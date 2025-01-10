<template>
    <section id='order'>
        <main>
            <nav>
                <ul>
                    <li v-for="(item, index) in res" :key="index"
                        @click="toDishPage(`/order/${route.params.desk_id}/${route.params.nanoId}/${item.id}`)"
                        :class="{ active: isActive(`/order/${route.params.desk_id}/${route.params.nanoId}/${item.id}`) }">
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
                <div class="info">
                    <ul>
                        <li v-for="(item, index) in completedOrders" :key="index" class="info-item">
                            <div>
                                <p>{{ item.name }}</p>
                                <span>×</span>
                                <p>{{ item.count }}</p>
                            </div>
                            <p :style="{ color: item.servedInfo.servedColor }">{{ item.servedInfo.servedText }}</p>
                        </li>
                    </ul>
                    <p>合計金額<span>{{ totalPrice }}</span>円</p>
                    <div class="btns">
                        <button @click.stop="closeAccounting">戻る</button>
                        <button @click.stop="openBillDialog">会計する</button>
                    </div>
                </div>
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
                                <span>{{ store?.dishes[route.params.desk_id][item.id]?.count || 0 }}</span>
                                <div class="add" @click="plus(item.id)">
                                    <p>＋</p>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <p>合計<span>{{ count.order }}</span>点</p>
                    <div class="btns">
                        <button @click.stop="closeCart">戻る</button>
                        <button @click.stop="api_addOrder()">注文する</button>
                    </div>
                </div>
                <div class="countOrder">{{ count.order }}</div>
            </div>
        </footer>
    </section>
    <BillDialog :isVisible="isVisible.bill" :totalPrice="totalPrice" @close="closeModal" @next="nextModal" />
    <WaitingDialog :isVisible="isVisible.waiting" @close="closeModal" />
    <ThankYouBusiness :isVisible="isVisible.thank" @close="closeModal" />
</template>

<script setup>
import { ref, reactive, onMounted, watch, onBeforeUnmount } from "vue"
import { fetDishCategory } from "@/api/orderApi";
import { useRouter, useRoute, RouterView } from "vue-router";
import { useDishStore } from "@/stores/dishStore";

const store = useDishStore();
const res = ref([]);
const route = useRoute();
const router = useRouter();
const emit = defineEmits(['fetchTables']);

/*************************************
* 画面モーダル設定
**************************************/
import BillDialog from "@/components/BillDialog.vue";
import WaitingDialog from "@/components/WaitingDialog.vue";
import ThankYouBusiness from "@/components/ThankYouBusiness.vue";
const isVisible = reactive({
    bill: false,
    waiting: false,
    thank: false
})
// モーダルを閉じる関数
const closeModal = () => {
    isVisible.bill = false;
    isVisible.waiting = false;
    isVisible.thank = false;
};

const nextModal = (payMethod) => {
    isVisible.bill = false;
    isVisible.waiting = true;
    finishOrderReq.amount = totalPrice.value
    finishOrderReq.payMethod = payMethod
    sendFinishOrder()
};

/*************************************
* 会計画面を開く
**************************************/
const openBillDialog = () => {
    if (checkIsAllPaid()) return
    isVisible.bill = true;
    isVisible.waiting = false;
}

const checkIsAllPaid = () => {
    if (completedOrders.value.length <= 0) {
        alert("注文していないので、会計できません。")
        return true
    }
    let count = 0
    completedOrders.value.forEach(item => {
        if (item.state !== '2') {
            count++;

        }
        return false;
    })

    if (count === 0) {
        return false;
    } else {
        const proceed = (confirm("料理がすべて提供されていないのですが、本当に会計するのですか？"))
        if (!proceed) {
            return true;
        } else {
            return false;
        }
    }
}


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


// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
    fetchDishCategories();
    api_getOrderTotalPrice();
    api_fetchAllCompletedOrders();
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
    active.accounting = true
}

const toCart = () => {
    active.shoppingCart = true
}

const closeCart = () => {
    active.shoppingCart = false
}

const closeAccounting = () => {
    active.accounting = false
}


const count = reactive({
    price: 0,
    order: 0
})


const infoList = ref([])
const sumOrderAndPrice = () => {
    count.order = store.sumDishCount(route.params.desk_id)
    count.price = totalPrice.value + store.sumDishPrice(route.params.desk_id)
    infoList.value = store.getInfo(route.params.desk_id);
};

const plus = (dishId) => {
    if (store.getDishCount(route.params.desk_id, dishId) < 10) {
        store.plusDishCount(route.params.desk_id, dishId)
        count.order = store.sumDishCount(route.params.desk_id)
        count.price = totalPrice.value + store.sumDishPrice(route.params.desk_id)
        infoList.value = store.getInfo(route.params.desk_id);
    }
}

const minus = (dishId) => {
    if (store.getDishCount(route.params.desk_id, dishId) > 0) {
        store.minusDishCount(route.params.desk_id, dishId)
        count.order = store.sumDishCount(route.params.desk_id)
        count.price = totalPrice.value + store.sumDishPrice(route.params.desk_id)
        infoList.value = store.getInfo(route.params.desk_id);
    }
}

//注文データをサーバに送信
import { addOrder } from "@/api/orderApi";
const req = reactive({
    deskId: route.params.desk_id,
    dishes: []
})
const api_addOrder = async () => {
    req.dishes = []
    infoList.value.forEach(item => {
        req.dishes.push({ dishId: item.id, count: item.count })
    })
    if (req.dishes.length === 0) {
        alert("注文項目がありません。")
        return;
    }
    try {
        const res = await addOrder(req);
        console.log(res);
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            console.log("注文成功");
            store.resetDishes(req.deskId)
            count.order = 0
            api_getOrderTotalPrice()
            api_fetchAllCompletedOrders();
            infoList.value = []
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

//オーダーの総値段を取得する
import { getOrderTotalPrice } from "@/api/orderApi";
const totalPrice = ref(0)
const api_getOrderTotalPrice = async () => {
    try {
        const res = await getOrderTotalPrice({ deskId: req.deskId });
        totalPrice.value = res.data.data
        count.price = totalPrice.value
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

//すべて注文完了オーダーを取得する
import { fetchAllCompletedOrders } from "@/api/orderApi";
const completedOrders = ref([])
const api_fetchAllCompletedOrders = async () => {
    try {
        const res = await fetchAllCompletedOrders({ deskId: req.deskId });
        completedOrders.value = res.data.data
        completedOrders.value.forEach(item => {
            item['servedInfo'] = setServedInfoByDishState(item.state)
        })
        console.log("注文完了オーダー", completedOrders.value);
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
}

/*************************************
* 料理状態により、表示変更
**************************************/
const setServedInfoByDishState = (state) => {
    const servedInfo = {
        servedText: "",
        servedColor: ""
    }
    if (state == '2') {
        servedInfo.servedText = '提供済'
        servedInfo.servedColor = 'green'
    } else {
        servedInfo.servedText = '未提供'
        servedInfo.servedColor = 'red'
    }
    return servedInfo
}

/*************************************
* 会計完了
**************************************/
import { finishOrder } from "@/api/orderApi";
const finishOrderReq = reactive({
    deskId: route.params.desk_id,
    payMethod: '',
    amount: 0
})
const sendFinishOrder = async () => {
    try {
        const res = await finishOrder(finishOrderReq);
        const code = res.data.code; // ステータスコードを取得
        console.log(code);
        if (code === 1) {
        } else {
            alert(res.data.msg); // エラーメッセージを表示
            console.log(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
        alert("リセット失敗しました。もう一度お試しください。");
    }
}


const props = defineProps(['orderState'])
watch(
    () => props.orderState,
    (newVal) => {
        if (newVal === '2') {
            isVisible.bill = false;
            isVisible.waiting = true;
        }
    },
    { immediate: true } // 立即执行，确保初始值也触发
);

/*************************************
* SSE（Server-Sent Events）を使用してリアルタイム更新を実現
**************************************/
import SseService from "@/utils/sseService";
/**
* SSE サービスのインスタンスを作成
* @param url サーバーの URL
* @param callback 新しいメッセージが届いた時の処理
*/
const sseService = new SseService("https://www.cyg1995.xyz/api/front/order", (event) => {
    console.log("收到的消息:", event);
    if (event === "提供済み") {
        // 注文リストをリフレッシュ
        api_fetchAllCompletedOrders();
    } else if (event === "リセット") {
        isVisible.waiting = false;
        isVisible.thank = true;
        setTimeout(() => {
            isVisible.thank = false;
            router.push(`/order/${route.params.desk_id}/0`);
        }, 5000)
    } else if (event === "呼び出しの状況を戻る成功") {
        emit('fetchTables');
    }
});
// SSE 接続を開始
sseService.connect();
//コンポーネントが破棄される前に呼び出されるフック
onBeforeUnmount(() => {
    sseService.close(); // SSE 接続を閉じる
});

</script>

<style lang="less" scoped>
@import '@/assets/css/phone_order.less';
</style>
