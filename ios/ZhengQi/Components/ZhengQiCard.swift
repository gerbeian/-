import SwiftUI

struct ZhengQiCard<Content: View>: View {
    let content: Content
    var padding: CGFloat = ZQSpacing.lg

    init(padding: CGFloat = ZQSpacing.lg, @ViewBuilder content: () -> Content) {
        self.padding = padding
        self.content = content()
    }

    var body: some View {
        content
            .padding(padding)
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.lg)
                    .fill(Color.canvas)
            )
    }
}

struct ZhengQiFrostedCard<Content: View>: View {
    let content: Content
    var padding: CGFloat = ZQSpacing.lg

    init(padding: CGFloat = ZQSpacing.lg, @ViewBuilder content: () -> Content) {
        self.padding = padding
        self.content = content()
    }

    var body: some View {
        content
            .padding(padding)
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.lg)
                    .fill(.ultraThinMaterial)
            )
    }
}

struct ZhengQiTile<Content: View>: View {
    let content: Content
    var isAlternate: Bool = false

    init(isAlternate: Bool = false, @ViewBuilder content: () -> Content) {
        self.isAlternate = isAlternate
        self.content = content()
    }

    var body: some View {
        content
            .padding(.horizontal, ZQSpacing.lg)
            .padding(.vertical, ZQSpacing.md)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(isAlternate ? Color.surfacePearl : Color.canvas)
    }
}