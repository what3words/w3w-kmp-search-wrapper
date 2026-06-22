// swift-tools-version:5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://api.github.com/repos/what3words/w3w-kmp-search-wrapper/releases/assets/454318306.zip"
let remoteKotlinChecksum = "7a63b3472752d37ad1cc2fd636a28770b306cc3840c8ad1452c7f8a85a24f100"
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