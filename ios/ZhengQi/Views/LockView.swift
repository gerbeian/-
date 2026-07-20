import SwiftUI

struct LockView: View {
    @State private var viewModel = LockViewModel()
    @State private var enteredPasscode = ""
    @State private var shakeCount = 0
    var onUnlock: () -> Void

    var body: some View {
        ZStack {
            Color.canvas.ignoresSafeArea()

            VStack(spacing: ZQSpacing.xxl) {
                Spacer()

                VStack(spacing: ZQSpacing.md) {
                    Image(systemName: "lock.shield.fill")
                        .font(.system(size: 56))
                        .foregroundColor(.primaryBlue)

                    Text("正气")
                        .font(.zqTitle)
                        .foregroundColor(.ink)

                    Text("请输入密码解锁")
                        .font(.zqSubtitle)
                }

                HStack(spacing: ZQSpacing.md) {
                    ForEach(0..<6, id: \.self) { index in
                        Circle()
                            .fill(index < enteredPasscode.count ? Color.primaryBlue : Color.hairline)
                            .frame(width: 14, height: 14)
                            .animation(.spring(response: 0.5, dampingFraction: 0.7), value: enteredPasscode.count)
                    }
                }
                .offset(x: CGFloat(shakeCount) * 4)
                .animation(.spring(response: 0.1, dampingFraction: 0.3), value: shakeCount)

                if viewModel.showError {
                    Text(viewModel.errorMessage)
                        .font(.zqSubtitle)
                        .foregroundColor(.red)
                        .transition(.opacity)
                }

                NumberPadView(
                    onTap: { digit in
                        if enteredPasscode.count < 6 {
                            enteredPasscode += digit
                            if enteredPasscode.count == 6 {
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                    if viewModel.verifyPassword(enteredPasscode) {
                                        onUnlock()
                                    } else {
                                        viewModel.showError = true
                                        viewModel.errorMessage = "密码错误，请重试"
                                        shakeCount = shakeCount == 0 ? 1 : (shakeCount == 1 ? -1 : 0)
                                        enteredPasscode = ""
                                    }
                                }
                            }
                        }
                    },
                    onDelete: {
                        if !enteredPasscode.isEmpty {
                            enteredPasscode.removeLast()
                            viewModel.showError = false
                        }
                    }
                )

                Spacer()
            }
            .padding(.bottom, ZQSpacing.xxl)
        }
    }
}