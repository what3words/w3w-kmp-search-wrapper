//
//  W3WAddressDetailViewController.swift
//  SearchComponent
//

import UIKit
import MapKit
import W3WSwiftCore
import W3WSwiftCoreSdk
import w3w


class W3WAddressDetailViewController: UIViewController {

  private let words: String
  private let nearestPlace: String

  private let wordsLabel = UILabel()
  private let nearestPlaceLabel = UILabel()
  private let mapView = MKMapView()

  init(words: String, nearestPlace: String) {
    self.words = words
    self.nearestPlace = nearestPlace
    super.init(nibName: nil, bundle: nil)
  }

  required init?(coder: NSCoder) { fatalError() }

  override func viewDidLoad() {
    super.viewDidLoad()
    setupUI()
    loadCoordinates()
  }

  private func setupUI() {
    view.backgroundColor = .systemBackground
    title = "///" + words

    navigationItem.rightBarButtonItem = UIBarButtonItem(
      barButtonSystemItem: .close,
      target: self,
      action: #selector(closeTapped)
    )

    wordsLabel.text = "///" + words
    wordsLabel.font = .systemFont(ofSize: 28, weight: .bold)
    wordsLabel.textColor = .systemRed
    wordsLabel.textAlignment = .center

    nearestPlaceLabel.text = nearestPlace
    nearestPlaceLabel.font = .systemFont(ofSize: 16)
    nearestPlaceLabel.textColor = .secondaryLabel
    nearestPlaceLabel.textAlignment = .center
    nearestPlaceLabel.numberOfLines = 2

    mapView.layer.cornerRadius = 12
    mapView.clipsToBounds = true

    let infoStack = UIStackView(arrangedSubviews: [wordsLabel, nearestPlaceLabel])
    infoStack.axis = .vertical
    infoStack.spacing = 6

    let mainStack = UIStackView(arrangedSubviews: [infoStack, mapView])
    mainStack.axis = .vertical
    mainStack.spacing = 20
    mainStack.translatesAutoresizingMaskIntoConstraints = false

    view.addSubview(mainStack)
    NSLayoutConstraint.activate([
      mainStack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
      mainStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
      mainStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
      mainStack.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
    ])
  }

  private func loadCoordinates() {
    DispatchQueue.global(qos: .userInitiated).async { [weak self] in
      guard let self else { return }
      guard let square = try? W3WSdk.sdk.convertToSquare(words: self.words),
            let coords = square.coordinates else { return }
      DispatchQueue.main.async {
        let region = MKCoordinateRegion(center: coords, latitudinalMeters: 200, longitudinalMeters: 200)
        self.mapView.setRegion(region, animated: false)
        let pin = MKPointAnnotation()
        pin.coordinate = coords
        pin.title = "///" + self.words
        self.mapView.addAnnotation(pin)
      }
    }
  }

  @objc private func closeTapped() {
    dismiss(animated: true)
  }
}
