import Foundation
import SwiftData

@Model
final class CheckIn {
    var id: UUID
    var trackItem: TrackItem?
    var date: String
    var status: Bool
    var note: String
    var imageData: Data?
    var createdAt: Date

    init(id: UUID = UUID(), trackItem: TrackItem? = nil, date: String = "", status: Bool = false, note: String = "", imageData: Data? = nil, createdAt: Date = Date()) {
        self.id = id
        self.trackItem = trackItem
        self.date = date
        self.status = status
        self.note = note
        self.imageData = imageData
        self.createdAt = createdAt
    }
}