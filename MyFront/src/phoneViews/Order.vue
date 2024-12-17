<template>
    <section id='order'>
        <main>
            <nav>
                <ul>
                    <li v-for="(item, index) in res" :key="index"
                        @click="toDishPage(`/order/${route.params.desk_id}/${item.id}`)"
                        :class="{ active: isActive(`/order/${route.params.desk_id}/${item.id}`) }">
                        <div class="nav_name">
                            <p :title="item.name">{{ item.name }}</p>
                        </div>
                    </li>
                </ul>
            </nav>
            <RouterView @sumOrderAndPrice="sumOrderAndPrice"></RouterView>
        </main>
        <footer>
            <div class="accounting" @click="toAccounting" :class="{ active: active.accounting }">
                <p>注文履歴</p>
            </div>
            <p class="price"><span>&yen;</span>{{ count.price }}</p>
            <div class="shoppingCart" :class="{ active: active.shoppingCart }" @click="toCart">
                <p>注文リスト</p>
                <div class="info">
                    <ul>
                        <li v-for="(item, index) in infoList" :key="index" class="info-item">
                            <p>{{ item.name }}</p>
                            <div class="count">
                                <div class="minus" @click="minus(item.id)">
                                    <p>－</p>
                                </div>
                                <span>{{ store.dishes[route.params.desk_id][item.id].count || 0 }}</span>
                                <div class="add" @click="plus(item.id)">
                                    <p>＋</p>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <p>合計<span>{{ count.order }}</span>点</p>
                    <div class="btns">
                        <button @click.stop="closeCart">キャンセル</button>
                        <button>注文する</button>
                    </div>
                </div>
                <div class="countOrder">{{ count.order }}</div>
            </div>
        </footer>
    </section>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue"
import { fetDishCategory, fetchAllTables } from "@/api/orderApi";
import { useRouter, useRoute, RouterView } from "vue-router";
import { useDishStore } from "@/stores/dishStore";

const store = useDishStore();
const res = ref([]);
const tables = ref([]);
const tableIdArray = ref([]);
const route = useRoute();
const router = useRouter();


// ディッシュカテゴリーを取得するメソッド
const fetchDishCategories = async () => {
    try {
        const response = await fetDishCategory();
        res.value = response.data.data; // APIからデータを取得
        console.log("dishCategories:", res.value);
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("料理カテゴリーのフェッチに失敗しました。もう一度お試しください。");
    }
};

const fetchTables = async () => {
    try {
        const response = await fetchAllTables();
        tables.value = response.data.data; // APIからデータを取得
        setTableIdArray()
        checkHasTable()
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
};

// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    fetchTables();
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

const active = reactive({
    accounting: false,
    shoppingCart: false
})

// const=isActive
const toAccounting = () => {
    active.accounting = !active.accounting
}

const toCart = () => {
    active.shoppingCart = true
}

const closeCart = () => {
    active.shoppingCart = false
}


const count = reactive({
    price: 0,
    order: 0
})


const infoList = ref([])
const sumOrderAndPrice = () => {
    count.order = store.sumDishCount(route.params.desk_id)
    count.price = store.sumDishPrice(route.params.desk_id)
    infoList.value = store.getInfo(route.params.desk_id);
};

const plus = (dishId) => {
    if (store.getDishCount(route.params.desk_id, dishId) < 10) {
        store.plusDishCount(route.params.desk_id, dishId)
        count.order = store.sumDishCount(route.params.desk_id)
        count.price = store.sumDishPrice(route.params.desk_id)
        infoList.value = store.getInfo(route.params.desk_id);
    }
}

const minus = (dishId) => {
    if (store.getDishCount(route.params.desk_id, dishId) > 0) {
        store.minusDishCount(route.params.desk_id, dishId)
        count.order = store.sumDishCount(route.params.desk_id)
        count.price = store.sumDishPrice(route.params.desk_id)
        infoList.value = store.getInfo(route.params.desk_id);
    }
}


const setTableIdArray = () => {
    tableIdArray.value = tables.value.map(
        (item) => item.id
    );
}
const checkHasTable = () => {
    console.log("tableId", route.params.desk_id);

    const flag = tableIdArray.value.includes(route.params.desk_id.toUpperCase());
    if (!flag) {
        router.push("/404");
    }
}

</script>

<style lang="less" scoped>
@import '@/assets/css/phone_order.less';
</style>
