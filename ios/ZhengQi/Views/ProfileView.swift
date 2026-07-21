import SwiftUI
import SwiftData

struct ProfileView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var viewModel = ProfileViewModel()
    @State private var showEditNickname = false
    @State private var showClearAlert = false
    @State private var showExportResult = false
    @State private var editedNickname = ""

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: ZQSpacing.lg) {
                    profileHeader
                    statsRow
                    functionsSection
                    dataSection
                    otherSection
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
                    viewModel.deleteAllData(context: modelContext)
                    viewModel.loadData(context: modelContext)
                }
            } message: {
                Text("此操作将删除所有打卡记录，且不可恢复。确定要继续吗？")
            }
            .alert(viewModel.exportResult?.contains("成功") == true ? "导出成功" : "导出失败", isPresented: $showExportResult) {
                Button("确定") { viewModel.clearExportResult() }
            } message: {
                Text(viewModel.exportResult ?? "")
            }
            .onChange(of: viewModel.exportResult) { _, newValue in
                if newValue != nil { showExportResult = true }
            }
        }
    }

    // MARK: - Profile Header (白底 + hairline 边框)
    private var profileHeader: some View {
        HStack(spacing: 16) {
            // Avatar
            Image(systemName: "person.crop.circle.fill")
                .font(.system(size: 48))
                .foregroundColor(.primaryBlue)
                .frame(width: 56, height: 56)
                .background(Circle().fill(Color.primaryBlue.opacity(0.1)))
                .overlay(
                    Circle()
                        .stroke(Color.hairline, lineWidth: 1)
                )

            VStack(alignment: .leading, spacing: 2) {
                Text(viewModel.level.name)
                    .font(.system(size: 20, weight: .semibold))
                Text("正气值 \(viewModel.zhengQiScore) · 连续 \(viewModel.consecutiveDays) 天")
                    .font(.system(size: 12))
                    .foregroundColor(.secondary)
            }
        }
        .padding(24)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(Color.canvas)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(Color.hairline, lineWidth: 1)
        )
    }

    // MARK: - Stats Row
    private var statsRow: some View {
        HStack(spacing: 0) {
            ProfileStatItem(label: "正气值", value: "\(viewModel.zhengQiScore)", icon: "star.fill")
            ProfileStatItem(label: "连续天数", value: "\(viewModel.consecutiveDays)", icon: "flame.fill")
            ProfileStatItem(label: "总打卡", value: "\(viewModel.totalCheckInDays)", icon: "checkmark.circle.fill")
        }
    }

    // MARK: - Functions Section
    private var functionsSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("功能")
                .font(.system(size: 20, weight: .semibold))

            NavigationLink(destination: StatisticsView()) {
                ProfileMenuItemRow(
                    icon: "chart.bar.fill",
                    title: "统计图表",
                    subtitle: "周频率 / 月视图 / 年视图"
                )
            }
            .buttonStyle(.plain)

            NavigationLink(destination: TrackItemConfigView()) {
                ProfileMenuItemRow(
                    icon: "square.and.pencil",
                    title: "追踪项配置",
                    subtitle: "自定义你的打卡项目"
                )
            }
            .buttonStyle(.plain)

            NavigationLink(destination: LockSetupView()) {
                ProfileMenuItemRow(
                    icon: "lock.shield",
                    title: "密码锁设置",
                    subtitle: "手势密码 / 数字密码"
                )
            }
            .buttonStyle(.plain)
        }
    }

    // MARK: - Data Section
    private var dataSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("数据")
                .font(.system(size: 20, weight: .semibold))

            Button {
                viewModel.exportData(context: modelContext)
            } label: {
                ProfileMenuItemRow(
                    icon: "square.and.arrow.down",
                    title: "导出数据",
                    subtitle: "将打卡数据导出为 JSON 文件"
                )
            }
            .buttonStyle(.plain)

            Button {
                showClearAlert = true
            } label: {
                ProfileMenuItemRow(
                    icon: "trash",
                    title: "清除数据",
                    subtitle: "删除所有打卡记录",
                    isDanger: true
                )
            }
            .buttonStyle(.plain)
        }
    }

    // MARK: - Other Section
    private var otherSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("其他")
                .font(.system(size: 20, weight: .semibold))

            ProfileMenuItemRow(
                icon: "info.circle",
                title: "关于",
                subtitle: "版本 1.0.0"
            )
        }
    }

    // MARK: - Edit Nickname Sheet
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

// MARK: - ProfileStatItem
struct ProfileStatItem: View {
    let label: String
    let value: String
    let icon: String

    var body: some View {
        VStack(spacing: 4) {
            Image(systemName: icon)
                .font(.system(size: 20))
                .foregroundColor(.primaryBlue)
            Text(value)
                .font(.system(size: 18, weight: .bold))
            Text(label)
                .font(.system(size: 12))
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
    }
}

// MARK: - ProfileMenuItemRow
struct ProfileMenuItemRow: View {
    let icon: String
    let title: String
    var subtitle: String = ""
    var isDanger: Bool = false
    @State private var isPressed = false

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.system(size: 18))
                .foregroundColor(isDanger ? .red : .primaryBlue)
                .frame(width: 24)

            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(isDanger ? .red : .primary)
                if !subtitle.isEmpty {
                    Text(subtitle)
                        .font(.system(size: 12))
                        .foregroundColor(.secondary)
                }
            }

            Spacer()

            Image(systemName: "chevron.right")
                .font(.system(size: 14, weight: .semibold))
                .foregroundColor(.secondary)
        }
        .padding(17)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(.ultraThinMaterial)
        )
        .scaleEffect(isPressed ? 0.98 : 1)
        .animation(.spring(response: 0.3, dampingFraction: 0.6), value: isPressed)
        .pressAction {
            withAnimation { isPressed = true }
        } onRelease: {
            withAnimation { isPressed = false }
        }
    }
}