import SwiftUI

struct QuoteCard: View {
    let quote: Quote
    let isFavorite: Bool
    let onFavorite: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            HStack {
                Image(systemName: "quote.opening")
                    .font(.system(size: 20))
                    .foregroundColor(.primaryBlue.opacity(0.3))

                Spacer()

                Button(action: onFavorite) {
                    Image(systemName: isFavorite ? "heart.fill" : "heart")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(isFavorite ? .red : .inkMuted48)
                }
                .animation(.spring(response: 0.5, dampingFraction: 0.7), value: isFavorite)
            }

            Text(quote.content)
                .font(.system(size: 17, weight: .regular))
                .foregroundColor(.ink)
                .lineSpacing(6)

            HStack {
                Text("—— \(quote.author)")
                    .font(.zqSubtitle)
                Spacer()
                Text(quote.category)
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(.primaryBlue)
                    .padding(.horizontal, ZQSpacing.sm)
                    .padding(.vertical, ZQSpacing.xxs)
                    .background(
                        RoundedRectangle(cornerRadius: ZQRounded.pill)
                            .fill(Color.primaryBlue.opacity(0.1))
                    )
            }
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }
}