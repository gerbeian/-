import SwiftUI

extension Color {
    static let primaryBlue = Color(hex: "0066cc")
    static let primaryFocus = Color(hex: "0071e3")
    static let primaryOnDark = Color(hex: "2997ff")
    static let ink = Color(hex: "1d1d1f")
    static let bodyOnDark = Color(hex: "ffffff")
    static let bodyMuted = Color(hex: "cccccc")
    static let inkMuted80 = Color(hex: "333333")
    static let inkMuted48 = Color(hex: "7a7a7a")
    static let dividerSoft = Color(hex: "f0f0f0")
    static let hairline = Color(hex: "e0e0e0")
    static let canvas = Color(hex: "ffffff")
    static let canvasParchment = Color(hex: "f5f5f7")
    static let surfacePearl = Color(hex: "fafafc")
    static let surfaceTile1 = Color(hex: "272729")
    static let surfaceTile2 = Color(hex: "2a2a2c")
    static let surfaceTile3 = Color(hex: "252527")
    static let surfaceBlack = Color(hex: "000000")

    static let checkGreen = Color(hex: "34c759")
    static let checkYellow = Color(hex: "ffcc00")
    static let checkGray = Color(hex: "c7c7cc")
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 6:
            (a, r, g, b) = (255, (int >> 16) & 0xFF, (int >> 8) & 0xFF, int & 0xFF)
        case 8:
            (a, r, g, b) = ((int >> 24) & 0xFF, (int >> 16) & 0xFF, (int >> 8) & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}