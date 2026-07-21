import SwiftUI

struct EmergencyView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var currentStep = 0
    @State private var resisted = false
    
    var body: some View {
        Group {
            if resisted {
                SuccessResistedView(onBack: { dismiss() })
            } else {
                ScrollView {
                    VStack(spacing: 24) {
                        AbstinenceCounterCard()
                            .padding(.top, 16)
                        
                        switch currentStep {
                        case 0:
                            BreathingGuideView(onComplete: { currentStep = 1 })
                        case 1:
                            DistractionListView(onNext: { currentStep = 2 })
                        case 2:
                            MotivationalQuotesView(onNext: { currentStep = 0 })
                        default:
                            EmptyView()
                        }
                        
                        HStack(spacing: 12) {
                            Button("返回") {
                                dismiss()
                            }
                            .buttonStyle(.bordered)
                            .frame(maxWidth: .infinity)
                            
                            Button {
                                AbstinenceManager.shared.recordResisted()
                                resisted = true
                            } label: {
                                HStack {
                                    Image(systemName: "shield.fill")
                                        .font(.system(size: 14))
                                    Text("我成功抵抗了")
                                }
                            }
                            .buttonStyle(.borderedProminent)
                            .tint(.checkGreen)
                            .frame(maxWidth: .infinity)
                        }
                        .padding(.bottom, 32)
                    }
                    .padding(.horizontal, 17)
                }
            }
        }
        .navigationTitle("应急模式")
        .navigationBarTitleDisplayMode(.large)
        .background(Color.canvasParchment)
    }
}

struct AbstinenceCounterCard: View {
    let manager = AbstinenceManager.shared
    var days: Int { manager.getCurrentStreak() }
    var stage: AbstinenceStage { manager.getStage() }
    var nextMilestone: (days: Int, label: String) { manager.getNextMilestone() }
    var progress: Double {
        nextMilestone.days > 0 ? min(Double(days) / Double(nextMilestone.days), 1.0) : 1.0
    }
    
    var body: some View {
        VStack(spacing: 8) {
            Text("已戒色")
                .font(.system(size: 14, weight: .semibold))
                .foregroundColor(.white.opacity(0.85))
            
            Text("\(days)")
                .font(.system(size: 56, weight: .bold))
                .foregroundColor(.white)
            
            Text("天")
                .font(.system(size: 20, weight: .semibold))
                .foregroundColor(.white.opacity(0.85))
            
            Text(stage.label)
                .font(.system(size: 16, weight: .semibold))
                .foregroundColor(.white.opacity(0.9))
            Text(stage.description)
                .font(.system(size: 12))
                .foregroundColor(.white.opacity(0.7))
            
            if nextMilestone.days > 0 {
                Text("距离「\(nextMilestone.label)」还有 \(nextMilestone.days - days) 天")
                    .font(.system(size: 13))
                    .foregroundColor(.white.opacity(0.8))
                    .padding(.top, 4)
                
                ProgressView(value: progress)
                    .tint(.white)
                    .background(Color.white.opacity(0.2))
                    .frame(height: 4)
                    .padding(.horizontal, 24)
            }
        }
        .padding(24)
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(Color.primaryBlue)
        )
    }
}

struct BreathingGuideView: View {
    let onComplete: () -> Void
    @State private var breathPhase = 0
    @State private var seconds = 4
    @State private var scale: CGFloat = 1
    @State private var opacity: Double = 0.3
    let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    @State private var phaseTimer = 0
    
    var body: some View {
        VStack(spacing: 16) {
            Text("深呼吸练习")
                .font(.system(size: 20, weight: .semibold))
            Text("跟着圆圈的节奏呼吸")
                .font(.system(size: 13))
                .foregroundColor(.secondary)
            
            ZStack {
                Circle()
                    .fill(Color.primaryBlue.opacity(opacity))
                    .frame(width: 160, height: 160)
                    .scaleEffect(scale)
                
                Text(breathPhase == 0 ? "吸气" : breathPhase == 1 ? "屏息" : "呼气")
                    .font(.system(size: 20, weight: .bold))
            }
            
            Text("\(seconds)秒")
                .font(.system(size: 18, weight: .semibold))
                .foregroundColor(.primaryBlue)
            
            Button("跳过呼吸练习 →") {
                onComplete()
            }
            .font(.system(size: 14))
            .foregroundColor(.secondary)
        }
        .padding(24)
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(.ultraThinMaterial)
        )
        .onReceive(timer) { _ in
            if seconds > 1 {
                seconds -= 1
            } else {
                seconds = 4
                if breathPhase == 0 {
                    breathPhase = 1
                } else if breathPhase == 1 {
                    breathPhase = 2
                    scale = 0.7
                } else {
                    breathPhase = 0
                    scale = 1
                }
            }
            
            withAnimation(.easeInOut(duration: 1)) {
                if breathPhase == 0 {
                    scale = 1.5
                    opacity = 0.8
                } else if breathPhase == 1 {
                    scale = 1.5
                    opacity = 0.8
                } else {
                    scale = 1.0
                    opacity = 0.3
                }
            }
        }
    }
}

struct DistractionListView: View {
    let onNext: () -> Void
    let distractions: [(String, String)] = [
        ("立刻做20个俯卧撑", "快速消耗体力，转移注意力"),
        ("用冷水洗脸", "冷水刺激能瞬间唤醒理性思维"),
        ("给家人或朋友打个电话", "社交连接能有效减少孤独感"),
        ("听一首喜欢的音乐", "音乐能迅速改变情绪状态"),
        ("出门散步10分钟", "换个环境，远离诱惑源"),
        ("大声朗读一段正气名言", "用声音强化内心的力量"),
        ("写下此刻的感受", "把冲动写出来，它就失去了力量"),
        ("做一组深呼吸", "深沉呼吸，让心回归平静")
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("分散注意力")
                .font(.system(size: 20, weight: .semibold))
            Text("立刻做以下任一件事，打断冲动")
                .font(.system(size: 13))
                .foregroundColor(.secondary)
            
            ForEach(distractions, id: \.0) { action, reason in
                HStack(spacing: 12) {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.primaryBlue)
                        .font(.system(size: 16))
                    VStack(alignment: .leading) {
                        Text(action)
                            .font(.system(size: 15, weight: .medium))
                        Text(reason)
                            .font(.system(size: 12))
                            .foregroundColor(.secondary)
                    }
                }
                .padding(.vertical, 4)
            }
            
            Button("下一步 →") {
                onNext()
            }
            .font(.system(size: 14))
            .foregroundColor(.secondary)
        }
        .padding(20)
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(.ultraThinMaterial)
        )
    }
}

struct MotivationalQuotesView: View {
    let onNext: () -> Void
    let quotes: [(String, String)] = [
        ("岂不闻天无绝人之路！只要我想走，路就在脚下。", "路在脚下"),
        ("这世间没有绝境，只有对处境绝望的人。解决问题的答案就在我们自己的手中。", "无绝境"),
        ("知人者智，自知者明。胜人者有力，自胜者强。", "老子"),
        ("养心莫善于寡欲。", "孟子"),
        ("正气存内，邪不可干。", "黄帝内经"),
        ("每一次战胜欲望，都是对灵魂的一次淬炼。", "修心"),
        ("你现在的坚持，会成为未来的铠甲。", "励志"),
        ("君子之道，暗然而日章。", "中庸")
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("正气名言")
                .font(.system(size: 20, weight: .semibold))
            Text("让古圣先贤的智慧给你力量")
                .font(.system(size: 13))
                .foregroundColor(.secondary)
            
            ForEach(quotes, id: \.0) { quote, author in
                VStack(alignment: .leading, spacing: 4) {
                    Text(quote)
                        .font(.system(size: 15))
                    Text("—— \(author)")
                        .font(.system(size: 12))
                        .foregroundColor(.primaryBlue)
                }
                .padding(.vertical, 4)
                Divider().opacity(0.3)
            }
            
            Button("回到呼吸练习 →") {
                onNext()
            }
            .font(.system(size: 14))
            .foregroundColor(.secondary)
        }
        .padding(20)
        .frame(maxWidth: .infinity)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(.ultraThinMaterial)
        )
    }
}

struct SuccessResistedView: View {
    let onBack: () -> Void
    @State private var showAnimation = false
    let manager = AbstinenceManager.shared
    
    var body: some View {
        VStack(spacing: 24) {
            Spacer()
            
            Circle()
                .fill(Color.checkGreen)
                .frame(width: 100, height: 100)
                .overlay(
                    Image(systemName: "shield.fill")
                        .font(.system(size: 48))
                        .foregroundColor(.white)
                )
                .scaleEffect(showAnimation ? 1 : 0.3)
                .animation(.spring(response: 0.4, dampingFraction: 0.4), value: showAnimation)
            
            Text("太棒了！")
                .font(.system(size: 28, weight: .bold))
            Text("你成功抵抗了一次诱惑")
                .font(.system(size: 16))
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
            
            HStack(spacing: 0) {
                StatItemView(value: "\(manager.getCurrentStreak())天", label: "当前戒色", icon: "flame.fill")
                StatItemView(value: "\(manager.resistedCount)次", label: "成功抵抗", icon: "shield.fill")
                StatItemView(value: "\(manager.longestStreak)天", label: "最长记录", icon: "trophy.fill")
            }
            .padding(.horizontal)
            
            Button {
                onBack()
            } label: {
                Text("返回首页")
                    .font(.system(size: 15, weight: .semibold))
                    .frame(maxWidth: .infinity)
                    .frame(height: 48)
                    .background(Color.primaryBlue)
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
            .padding(.horizontal, 32)
            
            Spacer()
        }
        .onAppear { showAnimation = true }
    }
}

struct StatItemView: View {
    let value: String
    let label: String
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