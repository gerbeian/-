<p align="center">
  <img src="assets/banner.svg" alt="正气" width="100%">
</p>

<p align="center">
  <a href="https://github.com/gerbeian/zhengqi/stargazers"><img src="https://img.shields.io/github/stars/gerbeian/zhengqi?color=0066cc&style=flat-square"></a>
  <a href="https://github.com/gerbeian/zhengqi/actions"><img src="https://img.shields.io/github/actions/workflow/status/gerbeian/zhengqi/ios-build.yml?color=0066cc&style=flat-square&label=iOS%20Build"></a>
  <a href="https://github.com/gerbeian/zhengqi/blob/main/LICENSE"><img src="https://img.shields.io/badge/license-MIT-0066cc?style=flat-square"></a>
  <br>
  <img src="https://img.shields.io/badge/Android-Kotlin-0066cc?logo=android&style=flat-square">
  <img src="https://img.shields.io/badge/iOS-Swift-0066cc?logo=apple&style=flat-square">
  <img src="https://img.shields.io/badge/Compose-UI-0066cc?logo=jetpackcompose&style=flat-square">
  <img src="https://img.shields.io/badge/SwiftUI-UI-0066cc?logo=swift&style=flat-square">
</p>

<p align="center">
  <img src="assets/divider.svg" width="60%">
</p>

<p align="center">
  <i>修身养性，正气存内。一款帮助你戒除不良习惯、坚持每日打卡、培养自律人生的双端 App。</i>
</p>

---

## 功能

<table>
<tr>
<td width="33%">

### 每日打卡

自定义追踪项目，自由增删排序

文字 + 图片双记录

一键批量打卡

</td>
<td width="33%">

### 戒色模式

戒色天数实时追踪，10 级里程碑

应急模式：深呼吸 + 注意力转移

曾国藩 / 了凡四训 / 断念法

</td>
<td width="33%">

### 正气值

总打卡 × 10 + 连续² × 2 + 完成率 × 50

五级成长：初心 → 修身 → 养性 → 正气 → 至善

</td>
</tr>
<tr>
<td width="33%">

### 学习模块

78 条儒释道经典名言

24 篇修心文章（戒色/修心/励志/养生）

收藏 + 搜索

</td>
<td width="33%">

### 统计图表

周频率柱状图

月趋势折线图

年打卡热力图

</td>
<td width="33%">

### 安全隐私

手势密码 / 数字密码

JSON 数据导出

完全本地存储，无需网络

</td>
</tr>
</table>

---

## 技术栈

| Platform | Language | UI | Database | Charts | Security |
|:--------:|:--------:|:--:|:--------:|:------:|:--------:|
| <img src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white&style=flat-square" height="18"> | Kotlin | Jetpack Compose | Room | MPAndroidChart | EncryptedSharedPreferences |
| <img src="https://img.shields.io/badge/iOS-000000?logo=apple&logoColor=white&style=flat-square" height="18"> | Swift | SwiftUI | SwiftData | Swift Charts | Keychain |

---

## 项目结构

<details>
<summary><b>点击展开完整目录树</b></summary>

```
正气/
├── android/
│   └── app/src/main/java/com/zhengqi/app/
│       ├── data/
│       │   ├── local/         # Room / DAO / AppDatabase
│       │   ├── repository/    # CheckInRepository / SeedData
│       │   └── security/      # LockManager / AbstinenceManager
│       ├── ui/
│       │   ├── screens/       # Home / Calendar / Learn / Profile / Emergency
│       │   ├── components/    # ProgressRing / LockPattern / TrackItemIcons
│       │   ├── navigation/    # NavGraph
│       │   └── theme/         # ColorScheme / Typography / Shape
│       └── viewmodel/
│
├── ios/ZhengQi/
│   ├── Models/                # CheckIn / TrackItem / Article / Quote
│   ├── Views/                 # Home / Calendar / Learn / Profile / Emergency
│   ├── ViewModels/
│   ├── Services/              # SeedData / Keychain / ZhengQiCalculator
│   ├── Components/            # ProgressRing / StatChart
│   └── Theme/                 # Colors / Shapes / Typography
│
├── .github/workflows/         # iOS IPA 自动构建
├── assets/                    # README 资源
├── DESIGN.md                  # 设计规范
└── README.md
```

</details>

---

## 快速开始

```bash
# Android
cd android && ./gradlew assembleDebug

# iOS（需 macOS + Xcode 15.4+）
cd ios && open ZhengQi.xcodeproj
```

> iOS IPA 通过 [GitHub Actions](https://github.com/gerbeian/zhengqi/actions) 自动构建，推送代码即触发。

---

## 设计规范

> 详见 [DESIGN.md](DESIGN.md)

| 类别 | 值 |
|:-----|:---|
| 强调色 | `#0066cc` |
| 风格 | 毛玻璃 · 无阴影 · 无渐变 |
| 字体 | SF Pro Display · 300 / 400 / 600 / 700 |
| 按钮 | Pill 药丸型 · spring 弹性 |
| 间距 | 17dp / 24dp 栅格 |

---

## 正气值

```
正气值 = 总打卡天数 × 10 + 连续天数² × 2 + 完成率 × 50
```

| 等级 | 所需正气值 | 称号 |
|:----:|:--------:|:----:|
| Lv.1 | 0 | 初心 |
| Lv.2 | 100 | 修身 |
| Lv.3 | 500 | 养性 |
| Lv.4 | 2000 | 正气 |
| Lv.5 | 5000 | 至善 |

---

## 路线图

- [x] 每日打卡 + 图片记录
- [x] 戒色模式 + 应急系统
- [x] 正气值 + 等级体系
- [x] 学习模块（名言 + 文章）
- [x] 统计图表
- [x] 密码锁
- [x] 数据导出
- [ ] 云端同步
- [ ] 社区功能
- [ ] Widget 桌面小组件
- [ ] Apple Watch 伴侣

---

## 许可

MIT © 2025