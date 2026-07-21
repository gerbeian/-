# 正气

修身养性，正气存内。一款帮助你戒除不良习惯、坚持每日打卡、培养自律人生的 App。

---

## 功能

- **每日打卡** — 自定义追踪项目，文字 + 图片记录，批量打卡
- **戒色模式** — 戒色天数追踪、里程碑进度、应急模式（深呼吸引导 / 分散注意力 / 正气名言）
- **正气值** — 基于打卡天数、连续天数、完成率综合计算，等级成长体系
- **日历视图** — 月度打卡热力图，一目了然
- **学习模块** — 儒释道经典名言 + 修心文章（戒色 / 修心 / 励志 / 养生）
- **统计图表** — 周频率 / 月趋势 / 年热力图
- **密码锁** — 手势密码 / 数字密码，保护隐私
- **数据导出** — JSON 格式导出打卡记录

## 技术栈

### Android
- Kotlin + Jetpack Compose
- Room Database
- Navigation Compose
- MPAndroidChart
- DataStore / EncryptedSharedPreferences

### iOS
- Swift + SwiftUI
- SwiftData
- Charts (Swift Charts)
- PhotosUI
- Keychain

## 项目结构

```
正气/
├── android/                    # Android 项目
│   └── app/src/main/java/com/zhengqi/app/
│       ├── data/               # 数据层 (Room, Repository, Security)
│       ├── ui/                 # UI 层 (Screens, Components, Navigation, Theme)
│       └── viewmodel/          # ViewModel
├── ios/                        # iOS 项目
│   └── ZhengQi/
│       ├── Models/             # 数据模型
│       ├── Views/              # 视图
│       ├── ViewModels/         # ViewModel
│       ├── Services/           # 服务层
│       ├── Components/         # 可复用组件
│       └── Theme/              # 设计令牌
└── .github/workflows/          # CI/CD
    └── ios-build.yml           # iOS IPA 自动构建
```

## 构建

### Android
```bash
cd android
./gradlew assembleDebug
```

### iOS
```bash
cd ios
xcodebuild archive \
  -project ZhengQi.xcodeproj \
  -scheme ZhengQi \
  -sdk iphoneos \
  -archivePath build/ZhengQi.xcarchive \
  CODE_SIGNING_ALLOWED=NO
```

iOS IPA 通过 GitHub Actions 自动构建，推送代码后自动触发。

## 设计规范

- 强调色 `#0066cc` (Action Blue)
- 无阴影 / 无渐变，毛玻璃材质
- SF Pro Display 字体系列
- Pill 按钮，spring 弹性动画
- 详见 [DESIGN.md](DESIGN.md)

## 正气值算法

```
正气值 = 总打卡天数 × 10 + 连续天数² × 2 + 完成率 × 50
```

等级体系：初心 → 修身 → 养性 → 正气 → 至善

## 许可

MIT License