// swift-tools-version:5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://api.github.com/repos/what3words/w3w-kmp-search-wrapper/releases/assets/412609922.zip"
let remoteKotlinChecksum = "b6dacf422ea3b8b0aad00cc3bc5cca6b2505ef26c1bae4de3ae42cda50ef2af2"
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