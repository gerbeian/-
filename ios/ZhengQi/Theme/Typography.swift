import SwiftUI

enum FontWeight {
    case light, regular, semibold, bold
}

extension Font {
    static let zqTitle: Font = .system(size: 26, weight: .bold)
    static let zqTitle2: Font = .system(size: 24, weight: .bold)
    static let zqSubtitle: Font = .system(size: 12, weight: .medium)
    static let zqBody: Font = .system(size: 16, weight: .regular)
    static let zqBodyBold: Font = .system(size: 16, weight: .semibold)
    static let zqCaption: Font = .system(size: 13, weight: .regular)
    static let zqLargeNumber: Font = .system(size: 48, weight: .bold)
    static let zqMediumNumber: Font = .system(size: 32, weight: .bold)
}

extension View {
    func zqTitleStyle() -> some View {
        self.font(.zqTitle).foregroundColor(.ink)
    }
    func zqTitle2Style() -> some View {
        self.font(.zqTitle2).foregroundColor(.ink)
    }
    func zqSubtitleStyle() -> some View {
        self.font(.zqSubtitle).foregroundColor(.inkMuted48)
    }
    func zqBodyStyle() -> some View {
        self.font(.zqBody).foregroundColor(.ink)
    }
    func zqCaptionStyle() -> some View {
        self.font(.zqCaption).foregroundColor(.inkMuted48)
    }
}