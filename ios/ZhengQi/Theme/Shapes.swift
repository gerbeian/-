import SwiftUI

enum ZQRounded {
    static let none: CGFloat = 0
    static let xs: CGFloat = 5
    static let sm: CGFloat = 8
    static let md: CGFloat = 11
    static let lg: CGFloat = 18
    static let pill: CGFloat = 9999
}

enum ZQSpacing {
    static let xxs: CGFloat = 4
    static let xs: CGFloat = 8
    static let sm: CGFloat = 12
    static let md: CGFloat = 17
    static let lg: CGFloat = 24
    static let xl: CGFloat = 32
    static let xxl: CGFloat = 48
    static let section: CGFloat = 80
}

extension View {
    func pillBackground() -> some View {
        self
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.pill)
                    .fill(Color.surfacePearl)
            )
    }
    func cardBackground() -> some View {
        self
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.lg)
                    .fill(Color.canvas)
            )
    }
    func frostedBackground() -> some View {
        self
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.lg)
                    .fill(.ultraThinMaterial)
            )
    }
}