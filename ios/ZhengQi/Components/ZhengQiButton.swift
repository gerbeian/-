import SwiftUI

struct ZhengQiButton: View {
    let title: String
    let action: () -> Void
    var isPrimary: Bool = true
    var isDisabled: Bool = false

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 16, weight: .semibold))
                .foregroundColor(isPrimary ? .white : .primaryBlue)
                .frame(maxWidth: .infinity)
                .padding(.vertical, ZQSpacing.md)
                .background(
                    RoundedRectangle(cornerRadius: ZQRounded.pill)
                        .fill(isPrimary ? Color.primaryBlue : Color.clear)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: ZQRounded.pill)
                        .stroke(isPrimary ? Color.clear : Color.primaryBlue, lineWidth: 1.5)
                )
        }
        .disabled(isDisabled)
        .opacity(isDisabled ? 0.5 : 1.0)
    }
}

struct ZhengQiIconButton: View {
    let iconName: String
    let action: () -> Void
    var isActive: Bool = false
    var size: CGFloat = 44

    var body: some View {
        Button(action: action) {
            Image(systemName: iconName)
                .font(.system(size: size * 0.45, weight: .semibold))
                .foregroundColor(isActive ? .white : .primaryBlue)
                .frame(width: size, height: size)
                .background(
                    RoundedRectangle(cornerRadius: ZQRounded.pill)
                        .fill(isActive ? Color.primaryBlue : Color.primaryBlue.opacity(0.1))
                )
        }
        .animation(.spring(response: 0.5, dampingFraction: 0.7), value: isActive)
    }
}