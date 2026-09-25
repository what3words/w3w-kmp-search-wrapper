// swift-tools-version:5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://api.github.com/repos/what3words/w3w-kmp-search-wrapper/releases/assets/588084002.zip"
let remoteKotlinChecksum = "1606f74537ee837ff3a2db68359edb12f44e607d3dc54ea9900e5bc03f2e9278"
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