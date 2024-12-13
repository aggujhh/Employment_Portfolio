<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="menu">
        <nav class="categories">
            <ul>
                <li v-for="(item, index) in res_list" :key="index" @click="toDishPage(`/menu/${item.id}`)"
                    :class="{ active: isActive(`/menu/${item.id}`) }">
                    <div class="nav_name">
                        <img src="@/assets/images/icon/dishIcon.png" />
                        <p :title="item.name">{{ item.name }}</p>
                    </div>
                    <div class="handle">
                        <span @click="() => { openUpdateDialog(item) }">修正</span>
                        <span @click="() => { openDeleteDialog(item) }">削除</span>
                    </div>
                </li>
                <li class="add" @click="() => { isVisible.add = true }">
                    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" viewBox="0 0 24 24" height="30px"
                        width="30px">
                        <path class="cls-1" fill="#3b3632"
                            d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM19,13h-6v6h-2v-6h-6v-2h6v-6h2v6h6v2Z" />
                        <path class="cls-2" fill="#fbb034"
                            d="M12,0C5.4,0,0,5.4,0,12s5.4,12,12,12,12-5.4,12-12S18.6,0,12,0ZM12,22c-5.5,0-10-4.5-10-10S6.5,2,12,2s10,4.5,10,10-4.5,10-10,10Z" />
                        <polygon class="cls-3" fill="#fbb034"
                            points="19 11 19 13 13 13 13 19 11 19 11 13 5 13 5 11 11 11 11 5 13 5 13 11 19 11" />
                    </svg>
                </li>
            </ul>
        </nav>
        <RouterView></RouterView>
        <AddDishCategory :isVisible="isVisible.add" @close="closeModal" @refresh="reloadPage" />
        <UpdateDishCategory :isVisible="isVisible.update" :name="dishCategorName" :id="dishCategorId"
            @close="closeModal" @refresh="reloadPage" />
        <DeleteDialog :isVisible="isVisible.delete" :name="dishCategorName" :deleteMethod="deleteMethod"
            @close="closeModal" />
    </section>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { fetDishCategory, deleteDishCategory } from "@/api/dishCategoryApi";
import AddDishCategory from "@/components/AddDishCategory.vue";
import UpdateDishCategory from "@/components/UpdateDishCategory.vue";

// ルーターとルートの取得
const router = useRouter();
const route = useRoute();

// リアクティブなデータの定義
const dishCategorName = ref(""); // カテゴリ名
const dishCategorId = ref(""); // カテゴリID
const res_list = ref([]); // カテゴリリスト
const temp_page = ref(""); // 一時的なページ保存

// モーダルの表示・非表示管理
const isVisible = reactive({
    add: false,
    delete: false,
    update: false,
});

// モーダルを閉じる関数
const closeModal = () => {
    isVisible.add = false;
    isVisible.delete = false;
    isVisible.update = false;
};

// 更新ダイアログを開く関数
const openUpdateDialog = (item) => {
    isVisible.update = true;
    dishCategorId.value = item.id;
    dishCategorName.value = item.name;
};

// 削除ダイアログを開く関数
const openDeleteDialog = (item) => {
    isVisible.delete = true;
    dishCategorId.value = item.id;
    dishCategorName.value = item.name;
};

// ルートのIDが変更された時に一時的なページ情報を保存
watch(
    () => route.params.id,
    (newValue, oldValue) => {
        temp_page.value = oldValue;
    }
);

// カテゴリ削除メソッド
const deleteMethod = async () => {
    try {
        // API を呼び出してカテゴリを削除
        const res = await deleteDishCategory({ id: dishCategorId.value });
        console.log(res);
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            alert(`カテゴリー「${dishCategorName.value}」が削除されました。`);
            closeModal();
            fetchDishCategories();
            toDishPage(`/menu/${temp_page.value}`);
        } else {
            alert("削除失敗しました。もう一度お試しください。");
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("削除失敗しました。もう一度お試しください。");
    }
};

// ディッシュカテゴリーを取得するメソッド
const fetchDishCategories = async () => {
    try {
        const response = await fetDishCategory();
        res_list.value = response.data.data; // APIからデータを取得
        console.log("dishCategories:", res_list.value);
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("料理カテゴリーのフェッチに失敗しました。もう一度お試しください。");
    }
};

// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    fetchDishCategories();
});

// 料理のページに移動する関数
const toDishPage = (path) => {
    router.push(path);
};

// 現在のルートがアクティブか確認する関数
const isActive = (path) => {
    return route.path === path;
};

// ページをリロードする関数
const reloadPage = async (value) => {
    console.log("ページを再読み込み中...");
    await fetchDishCategories(); // データを再取得
    if (value) {
        dishCategorId.value = value;
    } else {
        dishCategorId.value = res_list.value[res_list.value.length - 1].id;
    }
    console.log("newDishCategorId>", dishCategorId.value);
    toDishPage(`/menu/${dishCategorId.value}`);
};
</script>

<style lang="less" scoped>
@import '@/assets/css/menu.less';
</style>