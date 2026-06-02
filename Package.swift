// swift-tools-version:5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://api.github.com/repos/what3words/w3w-kmp-search-wrapper/releases/assets/436123548.zip"
let remoteKotlinChecksum = "8d942c00ad8f6201fbffbdaf4b9f523b1b58ce282ea9143e0a51d322c1c30d12"
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