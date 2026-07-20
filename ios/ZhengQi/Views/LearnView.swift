import SwiftUI
import SwiftData

struct LearnView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var viewModel = LearnViewModel()

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                Picker("分类", selection: $viewModel.selectedCategory) {
                    ForEach(viewModel.categories, id: \.self) { category in
                        Text(category).tag(category)
                    }
                }
                .pickerStyle(.segmented)
                .padding(.horizontal, ZQSpacing.lg)
                .padding(.vertical, ZQSpacing.sm)

                HStack {
                    Spacer()
                    Button(action: {
                        withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                            viewModel.showFavorites.toggle()
                        }
                    }) {
                        HStack(spacing: ZQSpacing.xxs) {
                            Image(systemName: viewModel.showFavorites ? "heart.fill" : "heart")
                                .foregroundColor(viewModel.showFavorites ? .red : .inkMuted48)
                            Text(viewModel.showFavorites ? "全部" : "收藏")
                                .font(.zqCaption)
                        }
                        .padding(.horizontal, ZQSpacing.sm)
                        .padding(.vertical, ZQSpacing.xxs)
                        .background(
                            RoundedRectangle(cornerRadius: ZQRounded.pill)
                                .fill(Color.primaryBlue.opacity(0.1))
                        )
                    }
                    .padding(.trailing, ZQSpacing.lg)
                }

                ScrollView {
                    VStack(spacing: ZQSpacing.lg) {
                        if viewModel.selectedCategory == "名言" || viewModel.showFavorites {
                            quotesSection
                        }

                        if viewModel.selectedCategory != "名言" || viewModel.showFavorites {
                            articlesSection
                        }
                    }
                    .padding(.horizontal, ZQSpacing.lg)
                    .padding(.bottom, ZQSpacing.section)
                }
            }
            .background(Color.canvasParchment)
            .navigationTitle("学习")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear { viewModel.loadData(context: modelContext) }
        }
    }

    private var quotesSection: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            if !viewModel.showFavorites {
                Text(viewModel.selectedCategory == "名言" ? "每日名言" : "\(viewModel.selectedCategory)名言")
                    .font(.zqTitle2)
            } else {
                Text("收藏名言")
                    .font(.zqTitle2)
            }

            if viewModel.filteredQuotes.isEmpty {
                emptyState(message: "暂无名言")
            } else {
                ForEach(viewModel.filteredQuotes) { quote in
                    QuoteCard(
                        quote: quote,
                        isFavorite: quote.isFavorite,
                        onFavorite: {
                            withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                                viewModel.toggleFavorite(quote: quote, context: modelContext)
                            }
                        }
                    )
                }
            }
        }
    }

    private var articlesSection: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            if !viewModel.showFavorites {
                Text("\(viewModel.selectedCategory)文章")
                    .font(.zqTitle2)
            } else {
                Text("收藏文章")
                    .font(.zqTitle2)
            }

            if viewModel.filteredArticles.isEmpty {
                emptyState(message: "暂无文章")
            } else {
                ForEach(viewModel.filteredArticles) { article in
                    NavigationLink(destination: ArticleDetailView(article: article)) {
                        articleRow(article)
                    }
                    .buttonStyle(.plain)
                    .simultaneousGesture(TapGesture().onEnded {
                        viewModel.incrementReadCount(article: article, context: modelContext)
                    })
                }
            }
        }
    }

    private func articleRow(_ article: Article) -> some View {
        VStack(alignment: .leading, spacing: ZQSpacing.xs) {
            HStack {
                Text(article.title)
                    .font(.system(size: 16, weight: .semibold))
                    .foregroundColor(.ink)
                    .lineLimit(1)
                Spacer()
                Button {
                    withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                        viewModel.toggleFavorite(article: article, context: modelContext)
                    }
                } label: {
                    Image(systemName: article.isFavorite ? "heart.fill" : "heart")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(article.isFavorite ? .red : .inkMuted48)
                }
            }

            Text(article.summary)
                .font(.zqCaption)
                .lineLimit(2)

            HStack {
                Text(article.category)
                    .font(.system(size: 11, weight: .medium))
                    .foregroundColor(.primaryBlue)
                    .padding(.horizontal, ZQSpacing.sm)
                    .padding(.vertical, ZQSpacing.xxs)
                    .background(
                        RoundedRectangle(cornerRadius: ZQRounded.pill)
                            .fill(Color.primaryBlue.opacity(0.1))
                    )

                Spacer()

                HStack(spacing: ZQSpacing.xxs) {
                    Image(systemName: "eye.fill")
                        .font(.system(size: 10))
                    Text("\(article.readCount)")
                        .font(.system(size: 11))
                }
                .foregroundColor(.inkMuted48)
            }
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }

    private func emptyState(message: String) -> some View {
        VStack(spacing: ZQSpacing.md) {
            Image(systemName: "tray")
                .font(.system(size: 32))
                .foregroundColor(.inkMuted48)
            Text(message)
                .font(.zqBody)
                .foregroundColor(.inkMuted48)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, ZQSpacing.xl)
    }
}