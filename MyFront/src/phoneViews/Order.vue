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
                        <button @click.stop="api_addOrder()">注文する</button>
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
    // URLのパラメーターからdesk_idを取得し、大文字に変換する
    const deskId = route.params.desk_id.toUpperCase();

    // テーブルリストから対象のテーブルを検索する
    const table = tables.value.find((item) => item.id === deskId);

    // テーブルが見つからない場合、404ページにリダイレクトする
    if (!table) {
        router.push("/404");
    } else {
        // テーブルのゲスト人数が4人の場合、注文ページにリダイレクトする
        if (table.guestCount === 0) {
            router.push(`/order/${route.params.desk_id}/0`);
        }
    }
};


//注文データをサーバに送信
import { addOrder } from "@/api/orderApi";
const req = reactive({
    deskId: route.params.desk_id,
    dishes: []
})
const api_addOrder = async () => {
    infoList.value.forEach(item => {
        req.dish.push({id:item.id,count:item.count})
    })
    try {
        const res = await addOrder(req);
        console.log(res);
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            console.log("注文成功");
            closeCart()
        } else {
            alert(res.data.msg);
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("注文失敗しました。もう一度お試しください。");
    }
}



</script>

<style lang="less" scoped>
@import '@/assets/css/phone_order.less';
</style>
