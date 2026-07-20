import SwiftUI

struct ArticleDetailView: View {
    let article: Article

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: ZQSpacing.lg) {
                Text(article.title)
                    .font(.zqTitle)
                    .padding(.horizontal, ZQSpacing.lg)

                HStack(spacing: ZQSpacing.md) {
                    Text(article.category)
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(.primaryBlue)
                        .padding(.horizontal, ZQSpacing.sm)
                        .padding(.vertical, ZQSpacing.xxs)
                        .background(
                            RoundedRectangle(cornerRadius: ZQRounded.pill)
                                .fill(Color.primaryBlue.opacity(0.1))
                        )

                    HStack(spacing: ZQSpacing.xxs) {
                        Image(systemName: "eye.fill")
                            .font(.system(size: 10))
                        Text("\(article.readCount) 次阅读")
                            .font(.zqSubtitle)
                    }

                    Spacer()
                }
                .padding(.horizontal, ZQSpacing.lg)

                Divider()
                    .background(Color.hairline)
                    .padding(.horizontal, ZQSpacing.lg)

                Text(article.content)
                    .font(.system(size: 16, weight: .regular))
                    .foregroundColor(.ink)
                    .lineSpacing(8)
                    .padding(.horizontal, ZQSpacing.lg)
                    .padding(.bottom, ZQSpacing.section)
            }
            .padding(.top, ZQSpacing.md)
        }
        .background(Color.canvas)
        .navigationTitle("文章详情")
        .navigationBarTitleDisplayMode(.inline)
    }
}