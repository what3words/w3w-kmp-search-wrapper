//
//  ViewController.swift
//  SearchComponent
//
//  Created by Dave Duprey on 11/03/2025.
//

import UIKit
import W3WSwiftCore
import W3WSwiftComponentsSearch


class ViewController: UIViewController, W3WEventSubscriberProtocol {
  var subscriptions = W3WEventsSubscriptions()
  
  @IBOutlet weak var button: UIButton!
  

  /// Set up the view
  override func viewDidLoad() {
    super.viewDidLoad()
    
    
    
    button.addTarget(self, action:#selector(buttonTapped), for: .touchUpInside)
    buttonTapped()
  }
  
  
  /// called when the search button is tapped
  @objc func buttonTapped() {
    DispatchQueue.main.async {
      self.showSearch()
    }
  }


  /// Set up the search
  func showSearch() {
    let kmpSearchTree = W3WKmpSearchTree()
    /// or using V4 HTTP API
   /// let KmpSdkBridge(apiKey: "YOUR_KEY")
    // make a mock search and add it to the KMP search using a W3WMultiSourceSearchTree
    //let mockSearchTree = SearchTree()
    //let searchTree = W3WMultiSourceSearchTree(searchTrees: [mockSearchTree, kmpSearchTree])

    // give the search tree to the search component
    let component = W3WSearchComponent(searchTree: kmpSearchTree, theme: .what3words)
    
    // show the component
    present(component, animated: true)

    // add placeholder text into the search field
    component.viewModel.input.send(.placeholder("Enter query here"))
    
    // react to events from the presenter/component
    subscribe(to: component.viewModel.output) { [weak self] event in
      switch event {
        case .dismiss:
          component.dismiss(animated: true)

        case .suggestion(let suggestion):
          component.dismiss(animated: true) {
            let words = suggestion.words ?? ""
            let nearestPlace = suggestion.nearestPlace ?? ""
            let detailVC = W3WAddressDetailViewController(words: words, nearestPlace: nearestPlace)
            let nav = UINavigationController(rootViewController: detailVC)
            self?.present(nav, animated: true)
          }

        case .address(_):
          break
        case .error(_):
          break
        case .cantFind:
          break
      }
    }
  }

}
