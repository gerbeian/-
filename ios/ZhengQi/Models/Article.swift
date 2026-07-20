import Foundation
import SwiftData

@Model
final class Article {
    var id: UUID
    var title: String
    var summary: String
    var content: String
    var category: String
    var isFavorite: Bool
    var readCount: Int
    var createdAt: Date

    init(id: UUID = UUID(), title: String, summary: String, content: String, category: String = "修心", isFavorite: Bool = false, readCount: Int = 0, createdAt: Date = Date()) {
        self.id = id
        self.title = title
        self.summary = summary
        self.content = content
        self.category = category
        self.isFavorite = isFavorite
        self.readCount = readCount
        self.createdAt = createdAt
    }
}