import Foundation

struct AbstinenceStage {
    let label: String
    let description: String
}

class AbstinenceManager {
    static let shared = AbstinenceManager()
    
    private let defaults = UserDefaults.standard
    
    private enum Keys {
        static let startDate = "abstinence_start_date"
        static let longestStreak = "longest_streak"
        static let totalRelapses = "total_relapses"
        static let resistedCount = "resisted_count"
        static let lastResetDate = "last_reset_date"
        static let goalDays = "goal_days"
    }
    
    private let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "yyyy-MM-dd"
        return f
    }()
    
    var startDate: Date? {
        get {
            guard let dateStr = defaults.string(forKey: Keys.startDate) else { return nil }
            return dateFormatter.date(from: dateStr)
        }
    }
    
    func getCurrentStreak() -> Int {
        guard let start = startDate else { return 0 }
        let calendar = Calendar.current
        let components = calendar.dateComponents([.day], from: start, to: Date())
        return components.day ?? 0
    }
    
    var longestStreak: Int {
        defaults.integer(forKey: Keys.longestStreak)
    }
    
    var totalRelapses: Int {
        defaults.integer(forKey: Keys.totalRelapses)
    }
    
    var resistedCount: Int {
        defaults.integer(forKey: Keys.resistedCount)
    }
    
    var goalDays: Int {
        get { defaults.integer(forKey: Keys.goalDays) }
        set { defaults.set(newValue, forKey: Keys.goalDays) }
    }
    
    func getGoalProgress() -> Double {
        let goal = goalDays
        if goal <= 0 { return 0 }
        return min(Double(getCurrentStreak()) / Double(goal), 1.0)
    }
    
    func startAbstinence() {
        let today = dateFormatter.string(from: Date())
        defaults.set(today, forKey: Keys.startDate)
    }
    
    func resetAbstinence() {
        let currentStreak = getCurrentStreak()
        if currentStreak > longestStreak {
            defaults.set(currentStreak, forKey: Keys.longestStreak)
        }
        defaults.set(totalRelapses + 1, forKey: Keys.totalRelapses)
        defaults.set(dateFormatter.string(from: Date()), forKey: Keys.lastResetDate)
        startAbstinence()
    }
    
    func recordResisted() {
        defaults.set(resistedCount + 1, forKey: Keys.resistedCount)
    }
    
    func getStage() -> AbstinenceStage {
        let days = getCurrentStreak()
        switch days {
        case ..<1: return AbstinenceStage(label: "戒色起步", description: "千里之行，始于足下")
        case ..<7: return AbstinenceStage(label: "第一周", description: "最难熬的阶段，坚持就是胜利")
        case ..<14: return AbstinenceStage(label: "一周达成", description: "身体开始适应，意志力在增强")
        case ..<21: return AbstinenceStage(label: "两周达成", description: "习惯开始形成，继续加油")
        case ..<30: return AbstinenceStage(label: "三周达成", description: "21天养成习惯，你已进入正轨")
        case ..<60: return AbstinenceStage(label: "一个月", description: "生理和心理都发生了积极变化")
        case ..<90: return AbstinenceStage(label: "两个月", description: "你已战胜了绝大多数人")
        case ..<180: return AbstinenceStage(label: "三个月", description: "自律已成为你的名片")
        case ..<365: return AbstinenceStage(label: "半年", description: "这是一个了不起的成就")
        default: return AbstinenceStage(label: "一年", description: "你已经脱胎换骨，正气长存")
        }
    }
    
    func getNextMilestone() -> (days: Int, label: String) {
        let days = getCurrentStreak()
        let milestones = [7, 14, 21, 30, 60, 90, 180, 365]
        for milestone in milestones {
            if days < milestone {
                return (milestone, formatMilestone(milestone))
            }
        }
        return (0, "已达成所有里程碑")
    }
    
    private func formatMilestone(_ days: Int) -> String {
        switch days {
        case 7: return "一周"
        case 14: return "两周"
        case 21: return "三周"
        case 30: return "一个月"
        case 60: return "两个月"
        case 90: return "三个月"
        case 180: return "半年"
        case 365: return "一年"
        default: return "\(days)天"
        }
    }
}