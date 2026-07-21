<p align="center">
  <img src="assets/banner.svg" alt="正气" width="800">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-Kotlin-0066cc?logo=android" alt="Android">
  <img src="https://img.shields.io/badge/iOS-Swift-0066cc?logo=apple" alt="iOS">
  <img src="https://img.shields.io/badge/Jetpack_Compose-UI-0066cc?logo=jetpackcompose" alt="Compose">
  <img src="https://img.shields.io/badge/SwiftUI-UI-0066cc?logo=swift" alt="SwiftUI">
  <img src="https://img.shields.io/badge/license-MIT-0066cc" alt="License">
</p>

---

修身养性，正气存内。一款帮助你戒除不良习惯、坚持每日打卡、培养自律人生的双端 App。

---

## 功能

<table>
<tr>
<td width="50%">

### 每日打卡
- 自定义追踪项目，自由增删排序
- 文字 + 图片双记录
- 一键批量打卡

### 戒色模式
- 戒色天数实时追踪
- 10 级里程碑进度条
- 应急模式：深呼吸引导 + 注意力转移 + 正气名言

</td>
<td width="50%">

### 正气值体系
- 多维度综合计算
- 5 级成长体系（初心 → 至善）
- 连续打卡激励

### 学习模块
- 78 条经典名言（儒释道）
- 24 篇修心文章（戒色/修心/励志/养生）
- 收藏 + 搜索

</td>
</tr>
</table>

## 技术栈

<table>
<tr>
<th width="50%"><img src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white" height="20"> Android</th>
<th width="50%"><img src="https://img.shields.io/badge/iOS-000000?logo=apple&logoColor=white" height="20"> iOS</th>
</tr>
<tr>
<td>

| 技术 | 用途 |
|------|------|
| Kotlin + Compose | UI 框架 |
| Room Database | 本地数据库 |
| Navigation Compose | 页面路由 |
| MPAndroidChart | 统计图表 |
| DataStore | 配置存储 |
| EncryptedSharedPreferences | 安全存储 |

</td>
<td>

| 技术 | 用途 |
|------|------|
| Swift + SwiftUI | UI 框架 |
| SwiftData | 本地数据库 |
| Swift Charts | 统计图表 |
| PhotosUI | 图片选择 |
| Keychain | 安全存储 |
| Combine | 响应式编程 |

</td>
</tr>
</table>

## 项目结构

```
正气/
├── android/                         # Android 项目
│   └── app/src/main/java/com/zhengqi/app/
│       ├── data/                    # 数据层
│       │   ├── local/               #   Room / DAO
│       │   ├── repository/          #   Repository / SeedData
│       │   └── security/            #   LockManager / AbstinenceManager
│       ├── ui/                      # UI 层
│       │   ├── screens/             #   Home / Calendar / Learn / Profile
│       │   ├── components/          #   可复用组件
│       │   ├── navigation/          #   导航图
│       │   └── theme/               #   ColorScheme / Typography / Shape
│       └── viewmodel/               # ViewModel
│
├── ios/                             # iOS 项目
│   └── ZhengQi/
│       ├── Models/                  # 数据模型
│       ├── Views/                   # 视图
│       ├── ViewModels/              # ViewModel
│       ├── Services/                # 服务层
│       ├── Components/              # 可复用组件
│       └── Theme/                   # 设计令牌
│
├── .github/workflows/               # CI/CD
│   └── ios-build.yml                # iOS IPA 自动构建
│
├── assets/                          # 资源文件
├── DESIGN.md                        # 设计规范
└── README.md
```

## 快速开始

```bash
# Android
cd android && ./gradlew assembleDebug

# iOS（需 macOS + Xcode）
cd ios && open ZhengQi.xcodeproj
```

> iOS IPA 通过 GitHub Actions 自动构建，推送代码即触发 → [Actions](https://github.com/gerbeian/zhengqi/actions)

## 设计规范

> 详见 [DESIGN.md](DESIGN.md)

| 类别 | 规范 |
|------|------|
| 强调色 | `#0066cc` (Action Blue) |
| 风格 | 无阴影 / 无渐变，毛玻璃材质 |
| 字体 | SF Pro Display，字重 300/400/600/700 |
| 按钮 | Pill 药丸型，spring 弹性动效 |
| 间距 | 17dp / 24dp 栅格系统 |

## 正气值算法

```
正气值 = 总打卡天数 × 10 + 连续天数² × 2 + 完成率 × 50
```

| 等级 | 所需正气值 |
|------|-----------|
| 初心 | 0 |
| 修身 | 100 |
| 养性 | 500 |
| 正气 | 2000 |
| 至善 | 5000 |

## 许可

MIT License