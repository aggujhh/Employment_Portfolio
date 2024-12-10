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
                        <p>{{ item.name }}</p>
                    </div>
                    <div class="handle">
                        <span>修正</span>
                        <span>削除</span>
                    </div>
                </li>
                <li class="add">
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
            <!-- 
            <button class="dishes" v-for="(category, index) in categories" :key="index"
                :class="{ active: activeIndex === index }" @click="toScreen(index)">
                <span>{{ category.name }}</span>
                <img src="../../../assets/dishIcon.png" />
                <span @click="ShowUpdateCategoryModal(index)">修正</span>
                <span @click="ShowDeleteCategoryModal(index)">削除</span>
            </button>
            <button class="dishes" @click="showAddCategoryModal = true">
                <div class="add">
                    <img src="@/assets/images/icon/add_btn.svg">
                </div>
            </button> -->
        </nav>
        <RouterView></RouterView>
    </section>
</template>

<script setup>
import { ref,onMounted } from "vue"
import { useRouter, useRoute } from "vue-router";
import { fetDishCategory } from "@/api/dishCategoryApi";

const res_list = ref([]); 

// fetDishCategory を呼び出してデータを取得するメソッドを定義
const fetchDishCategories = async () => {
    try {
        // API メソッドを呼び出す
        const response = await fetDishCategory();
        res_list.value = response.data.data; // API から返されたデータを dishCategories に格納
        console.log(res_list);

    } catch (err) {
        // リクエスト中にエラーが発生した場合の処理
        console.error("リクエストエラー:", err);
        alert("料理のカテゴリーのフェッチに失敗しました。もう一度お試しください。");
    }
};

// コンポーネントのロード時に呼び出し
onMounted(() => {
    fetchDishCategories(); // データ取得メソッドを呼び出す
});

const router = useRouter();
const route = useRoute();

// 料理のページへ
const toDishPage = (path) => {
    router.push(path);
};
// 現在のルートがアクティブかどうかを判断
const isActive = (path) => {
    return route.path === path;
};
</script>

<style lang="less" scoped>
@import '@/assets/css/menu.less';
</style>