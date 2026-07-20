import SwiftUI

struct LockPatternView: View {
    @Binding var enteredPasscode: String
    let maxDigits: Int
    let showError: Bool
    let errorMessage: String

    var body: some View {
        VStack(spacing: ZQSpacing.xl) {
            Image(systemName: "lock.shield.fill")
                .font(.system(size: 48))
                .foregroundColor(.primaryBlue)

            Text("请输入密码")
                .font(.zqTitle2)
                .foregroundColor(.ink)

            HStack(spacing: ZQSpacing.md) {
                ForEach(0..<maxDigits, id: \.self) { index in
                    Circle()
                        .fill(index < enteredPasscode.count ? Color.primaryBlue : Color.hairline)
                        .frame(width: 16, height: 16)
                        .animation(.spring(response: 0.5, dampingFraction: 0.7), value: enteredPasscode.count)
                }
            }

            if showError {
                Text(errorMessage)
                    .font(.zqSubtitle)
                    .foregroundColor(.red)
                    .transition(.opacity.combined(with: .move(edge: .top)))
            }

            NumberPadView(onTap: { digit in
                if enteredPasscode.count < maxDigits {
                    enteredPasscode += digit
                }
            }, onDelete: {
                if !enteredPasscode.isEmpty {
                    enteredPasscode.removeLast()
                }
            })
        }
        .padding(ZQSpacing.lg)
    }
}

struct NumberPadView: View {
    let onTap: (String) -> Void
    let onDelete: () -> Void

    private let numbers = [
        ["1", "2", "3"],
        ["4", "5", "6"],
        ["7", "8", "9"],
        ["", "0", "delete"]
    ]

    var body: some View {
        VStack(spacing: ZQSpacing.sm) {
            ForEach(numbers, id: \.self) { row in
                HStack(spacing: ZQSpacing.sm) {
                    ForEach(row, id: \.self) { key in
                        if key == "" {
                            Color.clear
                                .frame(width: 72, height: 56)
                        } else if key == "delete" {
                            Button(action: onDelete) {
                                Image(systemName: "delete.left.fill")
                                    .font(.system(size: 22))
                                    .foregroundColor(.inkMuted48)
                                    .frame(width: 72, height: 56)
                                    .background(
                                        RoundedRectangle(cornerRadius: ZQRounded.md)
                                            .fill(Color.canvasParchment)
                                    )
                            }
                        } else {
                            Button(action: { onTap(key) }) {
                                Text(key)
                                    .font(.system(size: 24, weight: .regular))
                                    .foregroundColor(.ink)
                                    .frame(width: 72, height: 56)
                                    .background(
                                        RoundedRectangle(cornerRadius: ZQRounded.md)
                                            .fill(Color.canvasParchment)
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}