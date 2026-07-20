import Foundation
import SwiftData

@Model
final class Quote {
    var id: UUID
    var content: String
    var author: String
    var category: String
    var isFavorite: Bool

    init(id: UUID = UUID(), content: String, author: String, category: String = "修心", isFavorite: Bool = false) {
        self.id = id
        self.content = content
        self.author = author
        self.category = category
        self.isFavorite = isFavorite
    }
}