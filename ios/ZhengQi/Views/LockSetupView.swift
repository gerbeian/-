import SwiftUI

struct LockSetupView: View {
    @AppStorage("lockEnabled") var lockEnabled: Bool = false
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var step: LockSetupStep = .enterPassword
    @State private var showError = false
    @State private var errorMessage = ""

    enum LockSetupStep {
        case enterPassword, confirmPassword
    }

    var body: some View {
        VStack(spacing: ZQSpacing.xl) {
            if !lockEnabled {
                existingPasswordSection
            } else {
                disableSection
            }
        }
        .padding(ZQSpacing.lg)
        .background(Color.canvasParchment)
        .navigationTitle("密码锁")
        .navigationBarTitleDisplayMode(.inline)
    }

    private var existingPasswordSection: some View {
        VStack(spacing: ZQSpacing.xl) {
            if KeychainManager.shared.read(key: "zhengqi_passcode") != nil {
                Text("密码锁已开启")
                    .font(.zqTitle2)
                    .foregroundColor(.checkGreen)

                Toggle("启用密码锁", isOn: $lockEnabled)
                    .tint(.primaryBlue)
                    .onChange(of: lockEnabled) { _, newValue in
                        if !newValue {
                            KeychainManager.shared.delete(key: "zhengqi_passcode")
                        }
                    }

                ZhengQiButton(title: "修改密码") {
                    step = .enterPassword
                    password = ""
                    confirmPassword = ""
                }
            } else {
                Text("设置密码锁保护您的隐私")
                    .font(.zqTitle2)
                    .multilineTextAlignment(.center)

                setupPasswordView
            }
        }
    }

    private var setupPasswordView: some View {
        VStack(spacing: ZQSpacing.lg) {
            switch step {
            case .enterPassword:
                VStack(spacing: ZQSpacing.md) {
                    Text("请设置密码（4-6位数字）")
                        .font(.zqSubtitle)

                    HStack(spacing: ZQSpacing.md) {
                        ForEach(0..<6, id: \.self) { index in
                            Circle()
                                .fill(index < password.count ? Color.primaryBlue : Color.hairline)
                                .frame(width: 14, height: 14)
                        }
                    }

                    NumberPadView(
                        onTap: { digit in
                            if password.count < 6 {
                                password += digit
                                if password.count >= 4 {
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                                        step = .confirmPassword
                                    }
                                }
                            }
                        },
                        onDelete: {
                            if !password.isEmpty {
                                password.removeLast()
                            }
                        }
                    )
                }

            case .confirmPassword:
                VStack(spacing: ZQSpacing.md) {
                    Text("请再次输入密码确认")
                        .font(.zqSubtitle)

                    HStack(spacing: ZQSpacing.md) {
                        ForEach(0..<6, id: \.self) { index in
                            Circle()
                                .fill(index < confirmPassword.count ? Color.primaryBlue : Color.hairline)
                                .frame(width: 14, height: 14)
                        }
                    }

                    if showError {
                        Text(errorMessage)
                            .font(.zqSubtitle)
                            .foregroundColor(.red)
                    }

                    NumberPadView(
                        onTap: { digit in
                            if confirmPassword.count < 6 {
                                confirmPassword += digit
                                if confirmPassword.count == password.count {
                                    if confirmPassword == password {
                                        if KeychainManager.shared.save(key: "zhengqi_passcode", value: KeychainManager.shared.hashPassword(password)) {
                                            lockEnabled = true
                                        }
                                    } else {
                                        showError = true
                                        errorMessage = "两次密码不一致"
                                        confirmPassword = ""
                                    }
                                }
                            }
                        },
                        onDelete: {
                            if !confirmPassword.isEmpty {
                                confirmPassword.removeLast()
                                showError = false
                            }
                        }
                    )

                    Button("重新设置") {
                        step = .enterPassword
                        password = ""
                        confirmPassword = ""
                        showError = false
                    }
                    .foregroundColor(.primaryBlue)
                    .font(.zqBody)
                }
            }
        }
    }

    private var disableSection: some View {
        VStack(spacing: ZQSpacing.xl) {
            Text("密码锁已开启")
                .font(.zqTitle2)
                .foregroundColor(.checkGreen)

            Toggle("启用密码锁", isOn: $lockEnabled)
                .tint(.primaryBlue)
                .onChange(of: lockEnabled) { _, newValue in
                    if !newValue {
                        KeychainManager.shared.delete(key: "zhengqi_passcode")
                    }
                }
        }
    }
}