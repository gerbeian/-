package com.zhengqi.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zhengqi.app.ui.theme.HairlineSoft
import com.zhengqi.app.ui.theme.PillShape
import com.zhengqi.app.ui.theme.StatusGreen

data class CommunityPost(
    val id: Long,
    val userName: String,
    val avatar: String,
    val title: String,
    val content: String,
    val streak: Int,
    val likes: Int,
    val comments: Int,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen() {
    val posts = remember {
        listOf(
            CommunityPost(
                id = 1, userName = "志强不息", avatar = "志",
                title = "连续打卡30天！",
                content = "坚持了一个月，每天早睡早起、运动阅读，感觉整个人的精气神都不一样了。正气值突破了500，非常有成就感！",
                streak = 30, likes = 128, comments = 23,
                time = "2小时前"
            ),
            CommunityPost(
                id = 2, userName = "静心修德", avatar = "静",
                title = "戒色50天的心得分享",
                content = "一开始很难，但坚持下来后，明显感觉精力更充沛了，思维也更清晰了。建议刚入门的兄弟们从每天冥想开始，慢慢来。",
                streak = 50, likes = 256, comments = 45,
                time = "5小时前"
            ),
            CommunityPost(
                id = 3, userName = "正气长存", avatar = "正",
                title = "早起100天，我变了",
                content = "从最初的赖床到现在的自然醒，早起已经成了习惯。每天多出来的两小时，我用来读书和锻炼，受益匪浅。",
                streak = 100, likes = 512, comments = 67,
                time = "昨天"
            ),
            CommunityPost(
                id = 4, userName = "知行合一", avatar = "知",
                title = "更新了运动记录",
                content = "今天跑了5公里，早睡早起身体好！坚持运动让我更加自律，戒色也变得更容易了。",
                streak = 15, likes = 64, comments = 8,
                time = "昨天"
            ),
            CommunityPost(
                id = 5, userName = "宁静致远", avatar = "宁",
                title = "修心感悟分享",
                content = "每天坚持冥想20分钟，读一段经典，感觉内心越来越平静。不再被外界的诱惑所干扰，这大概就是正气的力量吧。",
                streak = 75, likes = 189, comments = 32,
                time = "2天前"
            ),
            CommunityPost(
                id = 6, userName = "自律即自由", avatar = "自",
                title = "恢复健康的路上",
                content = "坚持了60天，身体各项指标都有改善。感谢这个平台，感谢大家的陪伴和鼓励。继续加油！",
                streak = 60, likes = 145, comments = 19,
                time = "3天前"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "社区",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Community stats header — Action Blue tile
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CommunityStatItem("会员", "3,286")
                    CommunityStatItem("今日打卡", "892")
                    CommunityStatItem("总打卡", "128,450")
                    CommunityStatItem("文章", "156")
                }
            }

            items(posts) { post ->
                CommunityPostCard(post = post)
            }
        }
    }
}

@Composable
private fun CommunityStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun CommunityPostCard(post: CommunityPost) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, HairlineSoft, MaterialTheme.shapes.large)
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = post.avatar,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = post.userName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = post.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            // Streak pill — pearl capsule
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(StatusGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = StatusGreen,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.streak}天",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatusGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = post.likes.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ChatBubbleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = post.comments.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "分享",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
