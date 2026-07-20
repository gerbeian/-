import SwiftUI

struct FrostedGlass: ViewModifier {
    var cornerRadius: CGFloat = ZQRounded.lg

    func body(content: Content) -> some View {
        content
            .background(
                RoundedRectangle(cornerRadius: cornerRadius)
                    .fill(.ultraThinMaterial)
            )
    }
}

extension View {
    func frostedGlass(cornerRadius: CGFloat = ZQRounded.lg) -> some View {
        modifier(FrostedGlass(cornerRadius: cornerRadius))
    }
}

struct FrostedOverlay: View {
    var body: some View {
        Rectangle()
            .fill(.ultraThinMaterial)
            .ignoresSafeArea()
    }
}