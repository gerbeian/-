import Foundation
import SwiftData

@Model
final class TrackItem {
    var id: UUID
    var name: String
    var iconName: String
    var isDefault: Bool
    var sortOrder: Int
    var isActive: Bool
    @Relationship(deleteRule: .cascade) var checkIns: [CheckIn]

    init(id: UUID = UUID(), name: String, iconName: String = "checkmark.circle.fill", isDefault: Bool = false, sortOrder: Int = 0, isActive: Bool = true) {
        self.id = id
        self.name = name
        self.iconName = iconName
        self.isDefault = isDefault
        self.sortOrder = sortOrder
        self.isActive = isActive
        self.checkIns = []
    }
}