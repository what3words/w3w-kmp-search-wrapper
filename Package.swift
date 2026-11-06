// swift-tools-version:5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://api.github.com/repos/what3words/w3w-kmp-search-wrapper/releases/assets/716935238.zip"
let remoteKotlinChecksum = "7ad86241527b0f307ed833faec50553dc31d5e7afafd642cf3a3ef52a8dcee95"
let packageName = "W3WKotlinSearchWrapper"
// END KMMBRIDGE BLOCK

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        )
        ,
    ]
)