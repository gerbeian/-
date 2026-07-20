import SwiftUI
import SwiftData

struct TrackItemConfigView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var trackItems: [TrackItem] = []
    @State private var showAddSheet = false
    @State private var newItemName = ""
    @State private var newItemIcon = "checkmark.circle.fill"

    let iconOptions = [
        "shield.fill", "figure.run", "moon.zzz.fill", "book.fill",
        "brain.head.profile", "heart.fill", "leaf.fill", "drop.fill",
        "sun.max.fill", "star.fill", "flame.fill", "sparkles",
        "pencil", "music.note", "house.fill", "car.fill"
    ]

    var body: some View {
        VStack(spacing: 0) {
            List {
                ForEach(trackItems) { item in
                    HStack(spacing: ZQSpacing.md) {
                        Image(systemName: item.iconName)
                            .foregroundColor(.primaryBlue)
                            .frame(width: 28)

                        TextField("名称", text: Binding(
                            get: { item.name },
                            set: { newValue in
                                item.name = newValue
                                try? modelContext.save()
                            }
                        ))
                        .font(.zqBody)

                        Toggle("", isOn: Binding(
                            get: { item.isActive },
                            set: { newValue in
                                item.isActive = newValue
                                try? modelContext.save()
                            }
                        ))
                        .tint(.primaryBlue)
                        .labelsHidden()
                    }
                }
                .onDelete(perform: deleteItems)
            }
            .listStyle(.plain)
        }
        .background(Color.canvasParchment)
        .navigationTitle("打卡项目")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button(action: { showAddSheet = true }) {
                    Image(systemName: "plus")
                        .foregroundColor(.primaryBlue)
                }
            }
        }
        .onAppear { loadItems() }
        .sheet(isPresented: $showAddSheet) {
            addItemSheet
        }
    }

    private func loadItems() {
        let desc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? modelContext.fetch(desc)) ?? []
    }

    private func deleteItems(at offsets: IndexSet) {
        for index in offsets {
            let item = trackItems[index]
            modelContext.delete(item)
        }
        try? modelContext.save()
        loadItems()
    }

    private var addItemSheet: some View {
        NavigationStack {
            VStack(spacing: ZQSpacing.lg) {
                TextField("项目名称", text: $newItemName)
                    .font(.zqBody)
                    .padding(ZQSpacing.md)
                    .background(
                        RoundedRectangle(cornerRadius: ZQRounded.md)
                            .fill(Color.canvasParchment)
                    )

                Text("选择图标")
                    .font(.zqSubtitle)
                    .frame(maxWidth: .infinity, alignment: .leading)

                LazyVGrid(columns: Array(repeating: GridItem(.flexible()), count: 4), spacing: ZQSpacing.sm) {
                    ForEach(iconOptions, id: \.self) { icon in
                        Button(action: { newItemIcon = icon }) {
                            Image(systemName: icon)
                                .font(.system(size: 24))
                                .foregroundColor(newItemIcon == icon ? .white : .primaryBlue)
                                .frame(width: 52, height: 52)
                                .background(
                                    RoundedRectangle(cornerRadius: ZQRounded.md)
                                        .fill(newItemIcon == icon ? Color.primaryBlue : Color.primaryBlue.opacity(0.1))
                                )
                        }
                    }
                }

                ZhengQiButton(title: "添加", isDisabled: newItemName.isEmpty) {
                    addItem()
                }
            }
            .padding(ZQSpacing.lg)
            .navigationTitle("新增项目")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("取消") { showAddSheet = false }
                }
            }
        }
        .presentationDetents([.medium])
    }

    private func addItem() {
        guard !newItemName.isEmpty else { return }
        let maxOrder = trackItems.map(\.sortOrder).max() ?? 0
        let item = TrackItem(name: newItemName, iconName: newItemIcon, isDefault: false, sortOrder: maxOrder + 1, isActive: true)
        modelContext.insert(item)
        try? modelContext.save()
        newItemName = ""
        newItemIcon = "checkmark.circle.fill"
        showAddSheet = false
        loadItems()
    }
}