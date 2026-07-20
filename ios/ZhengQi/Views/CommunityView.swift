import SwiftUI

struct CommunityView: View {
    @State private var milestones: [CommunityMilestone] = CommunityMilestone.sampleData

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: ZQSpacing.lg) {
                    headerCard

                    LazyVStack(spacing: ZQSpacing.md) {
                        ForEach(milestones) { milestone in
                            milestoneCard(milestone)
                        }
                    }
                }
                .padding(.horizontal, ZQSpacing.lg)
                .padding(.bottom, ZQSpacing.section)
            }
            .background(Color.canvasParchment)
            .navigationTitle("社区")
            .navigationBarTitleDisplayMode(.inline)
        }
    }

    private var headerCard: some View {
        VStack(spacing: ZQSpacing.md) {
            Image(systemName: "person.3.fill")
                .font(.system(size: 36))
                .foregroundColor(.primaryBlue)

            Text("正气社区")
                .font(.zqTitle2)

            Text("与同道中人一起修身养性，互相鼓励")
                .font(.zqCaption)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }

    private func milestoneCard(_ milestone: CommunityMilestone) -> some View {
        HStack(alignment: .top, spacing: ZQSpacing.md) {
            Circle()
                .fill(Color.primaryBlue.opacity(0.1))
                .frame(width: 44, height: 44)
                .overlay(
                    Image(systemName: milestone.icon)
                        .foregroundColor(.primaryBlue)
                        .font(.system(size: 18))
                )

            VStack(alignment: .leading, spacing: ZQSpacing.xxs) {
                Text(milestone.username)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundColor(.ink)

                Text(milestone.content)
                    .font(.zqBody)
                    .foregroundColor(.inkMuted80)

                Text(milestone.timeAgo)
                    .font(.zqSubtitle)
            }

            Spacer()
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }
}

struct CommunityMilestone: Identifiable {
    let id = UUID()
    let username: String
    let content: String
    let icon: String
    let timeAgo: String

    static let sampleData: [CommunityMilestone] = [
        CommunityMilestone(username: "正气少年", content: "连续打卡 30 天！感觉精力充沛了很多，继续坚持！", icon: "flame.fill", timeAgo: "2小时前"),
        CommunityMilestone(username: "静心修行", content: "今日冥想20分钟，内心平静如水。", icon: "brain.head.profile", timeAgo: "3小时前"),
        CommunityMilestone(username: "自强不息", content: "完成了今日所有打卡目标，正气值突破500！", icon: "star.fill", timeAgo: "5小时前"),
        CommunityMilestone(username: "健康生活", content: "早起跑步5公里，一天都精神饱满！", icon: "figure.run", timeAgo: "8小时前"),
        CommunityMilestone(username: "读书明理", content: "本月读完第3本书，读书使人明智。", icon: "book.fill", timeAgo: "昨天"),
        CommunityMilestone(username: "知行合一", content: "达到「正气浩然」等级，共勉！", icon: "bolt.fill", timeAgo: "昨天"),
        CommunityMilestone(username: "静心养性", content: "连续早睡7天，皮肤都变好了！", icon: "moon.zzz.fill", timeAgo: "2天前"),
        CommunityMilestone(username: "守正不阿", content: "戒色第60天，身心焕然一新。", icon: "shield.fill", timeAgo: "2天前")
    ]
}