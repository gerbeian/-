import Foundation
import SwiftData
import SwiftUI

@Observable
class LearnViewModel {
    var selectedCategory: String = "名言"
    var quotes: [Quote] = []
    var articles: [Article] = []
    var favoriteQuotes: [Quote] = []
    var favoriteArticles: [Article] = []
    var showFavorites = false

    let categories = ["名言", "修心", "励志", "养生"]

    func loadData(context: ModelContext) {
        let quoteDesc = FetchDescriptor<Quote>()
        quotes = (try? context.fetch(quoteDesc)) ?? []

        let articleDesc = FetchDescriptor<Article>(sortBy: [SortDescriptor(\.createdAt, order: .reverse)])
        articles = (try? context.fetch(articleDesc)) ?? []

        favoriteQuotes = quotes.filter { $0.isFavorite }
        favoriteArticles = articles.filter { $0.isFavorite }
    }

    var filteredQuotes: [Quote] {
        if showFavorites { return favoriteQuotes }
        if selectedCategory == "名言" { return quotes }
        return quotes.filter { $0.category == selectedCategory }
    }

    var filteredArticles: [Article] {
        if showFavorites { return favoriteArticles }
        if selectedCategory == "名言" { return [] }
        return articles.filter { $0.category == selectedCategory }
    }

    func toggleFavorite(quote: Quote, context: ModelContext) {
        quote.isFavorite.toggle()
        try? context.save()
        loadData(context: context)
    }

    func toggleFavorite(article: Article, context: ModelContext) {
        article.isFavorite.toggle()
        try? context.save()
        loadData(context: context)
    }

    func incrementReadCount(article: Article, context: ModelContext) {
        article.readCount += 1
        try? context.save()
    }
}