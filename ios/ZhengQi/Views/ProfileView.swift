import SwiftUI
import SwiftData

struct ProfileView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var viewModel = ProfileViewModel()
    @State private var showEditNickname = false
    @State private var showClearAlert = false
    @State private var editedNickname = ""

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: ZQSpacing.lg) {
                    profileHeader
                    scoreCard
                    statsEntry
                    settingsSection
                    aboutSection
                    Spacer().frame(height: ZQSpacing.section)
                }
                .padding(.horizontal, ZQSpacing.lg)
            }
            .background(Color.canvasParchment)
            .navigationTitle("我的")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear { viewModel.loadData(context: modelContext) }
            .sheet(isPresented: $showEditNickname) {
                editNicknameSheet
            }
            .alert("确认清除", isPresented: $showClearAlert) {
                Button("取消", role: .cancel) {}
                Button("清除", role: .destructive) {
                    viewModel.clearAllData(context: modelContext)
                    viewModel.loadData(context: modelContext)
                }
            } message: {
                Text("将清除所有数据并恢复默认设置，此操作不可撤销。")
            }
        }
    }

    private var profileHeader: some View {
        VStack(spacing: ZQSpacing.md) {
            ZStack {
                Circle()
                    .fill(viewModel.avatarColor)
                    .frame(width: 72, height: 72)

                Text(String(viewModel.nickname.prefix(1)))
                    .font(.system(size: 28, weight: .bold))
                    .foregroundColor(.white)
            }

            Button(action: {
                editedNickname = viewModel.nickname
                showEditNickname = true
            }) {
                HStack(spacing: ZQSpacing.xxs) {
                    Text(viewModel.nickname)
                        .font(.zqTitle2)
                    Image(systemName: "pencil")
                        .font(.system(size: 13))
                        .foregroundColor(.primaryBlue)
                }
            }

            HStack(spacing: ZQSpacing.xs) {
                Image(systemName: viewModel.level.icon)
                    .font(.system(size: 14))
                Text(viewModel.level.name)
                    .font(.system(size: 13, weight: .semibold))
            }
            .foregroundColor(.primaryBlue)
            .padding(.horizontal, ZQSpacing.md)
            .padding(.vertical, ZQSpacing.xs)
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.pill)
                    .fill(Color.primaryBlue.opacity(0.1))
            )
        }
        .frame(maxWidth: .infinity)
        .padding(.top, ZQSpacing.md)
    }

    private var scoreCard: some View {
        HStack(spacing: ZQSpacing.lg) {
            statItem(value: "\(viewModel.zhengQiScore)", label: "正气值", color: .primaryBlue)
            Divider().frame(height: 40).background(Color.hairline)
            statItem(value: "\(viewModel.consecutiveDays)", label: "连续天数", color: .ink)
            Divider().frame(height: 40).background(Color.hairline)
            statItem(value: "\(viewModel.totalCheckInDays)", label: "总打卡", color: .ink)
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }

    private func statItem(value: String, label: String, color: Color) -> some View {
        VStack(spacing: ZQSpacing.xxs) {
            Text(value)
                .font(.system(size: 22, weight: .bold))
                .foregroundColor(color)
            Text(label)
                .font(.zqSubtitle)
        }
        .frame(maxWidth: .infinity)
    }

    private var statsEntry: some View {
        NavigationLink(destination: StatisticsView()) {
            HStack {
                Image(systemName: "chart.bar.fill")
                    .foregroundColor(.primaryBlue)
                    .font(.system(size: 18))
                Text("统计图表")
                    .font(.zqBody)
                    .foregroundColor(.ink)
                Spacer()
                Image(systemName: "chevron.right")
                    .font(.system(size: 14, weight: .semibold))
                    .foregroundColor(.inkMuted48)
            }
            .padding(ZQSpacing.lg)
            .background(
                RoundedRectangle(cornerRadius: ZQRounded.lg)
                    .fill(.ultraThinMaterial)
            )
        }
        .buttonStyle(.plain)
    }

    private var settingsSection: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("设置")
                .font(.zqTitle2)

            VStack(spacing: 0) {
                NavigationLink(destination: TrackItemConfigView()) {
                    ZhengQiTile {
                        HStack {
                            Image(systemName: "list.bullet.clipboard")
                                .foregroundColor(.primaryBlue)
                                .frame(width: 24)
                            Text("打卡项目配置")
                                .font(.zqBody)
                                .foregroundColor(.ink)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.inkMuted48)
                        }
                    }
                }
                .buttonStyle(.plain)

                NavigationLink(destination: LockSetupView()) {
                    ZhengQiTile(isAlternate: true) {
                        HStack {
                            Image(systemName: "lock.shield")
                                .foregroundColor(.primaryBlue)
                                .frame(width: 24)
                            Text("密码锁")
                                .font(.zqBody)
                                .foregroundColor(.ink)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.inkMuted48)
                        }
                    }
                }
                .buttonStyle(.plain)

                Button(action: { showClearAlert = true }) {
                    ZhengQiTile(isAlternate: false) {
                        HStack {
                            Image(systemName: "trash")
                                .foregroundColor(.red)
                                .frame(width: 24)
                            Text("清除数据")
                                .font(.zqBody)
                                .foregroundColor(.red)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.inkMuted48)
                        }
                    }
                }
                .buttonStyle(.plain)
            }
            .clipShape(RoundedRectangle(cornerRadius: ZQRounded.lg))
        }
    }

    private var aboutSection: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("关于")
                .font(.zqTitle2)

            VStack(spacing: 0) {
                ZhengQiTile {
                    HStack {
                        Text("版本")
                            .font(.zqBody)
                        Spacer()
                        Text("1.0.0")
                            .font(.zqCaption)
                    }
                }
                ZhengQiTile(isAlternate: true) {
                    HStack {
                        Text("正气")
                            .font(.zqBody)
                        Spacer()
                        Text("修身养性，正气存内")
                            .font(.zqCaption)
                    }
                }
            }
            .clipShape(RoundedRectangle(cornerRadius: ZQRounded.lg))
        }
    }

    private var editNicknameSheet: some View {
        NavigationStack {
            VStack(spacing: ZQSpacing.xl) {
                Text("修改昵称")
                    .font(.zqTitle2)

                TextField("请输入昵称", text: $editedNickname)
                    .font(.zqBody)
                    .padding(ZQSpacing.md)
                    .background(
                        RoundedRectangle(cornerRadius: ZQRounded.md)
                            .fill(Color.canvasParchment)
                    )

                ZhengQiButton(title: "保存") {
                    viewModel.nickname = editedNickname
                    viewModel.saveNickname()
                    showEditNickname = false
                }
            }
            .padding(ZQSpacing.lg)
            .navigationTitle("编辑昵称")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("取消") { showEditNickname = false }
                }
            }
        }
        .presentationDetents([.medium])
    }
}