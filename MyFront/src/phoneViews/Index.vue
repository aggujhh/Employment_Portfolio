<template>
    <section id='order'>
        <header class="title">
            <div>
                <p>メ</p>
            </div>
            <div>
                <p>ニ</p>
            </div>
            <div>
                <p>ュ</p>
            </div>
            <div>
                <p>ー</p>
            </div>
        </header>
        <!-- <div class="deskNumber" @click="visible">
            {{ $store.state.deskId }}番テーブル
            <div class="dropdown" :class="{ active: isActive }">
                <ul draggable="false">
                    <li v-for="(num, i) in numbers" :key="i" @click="chooseDesk(i)">
                        {{ num }}番テーブル
                    </li>
                </ul>
            </div>
        </div> -->
        <main>
            <nav>
                <ul>
                    <!-- <li v-for="(category, index) in categories" :key="index"
                    :class="{ navActive: $store.state.dishViewIndex === index }" @click="toScreen(index)">
                    {{ category.name }}
                </li> -->
                    <li v-for="(item, index) in res" :key="index" @click="toDishPage(`/order/${item.id}`)"
                        :class="{ active: isActive(`/order/${item.id}`) }">
                        <div class="nav_name">
                            <p :title="item.name">{{ item.name }}</p>
                        </div>
                    </li>
                </ul>
            </nav>
            <RouterView></RouterView>
        </main>
        
        <!-- <div class="main"><router-view /></div>
        <footer :class="{ accountingActive: isAccountingActive }">
            <button class="accounting" @click="toAccounting">会計する</button>
            <button class="shoppingCart" :class="{ cartActive: isCartActive }" @click="toCart">
                <div class="orderCount">{{ countNumber }}</div>
                <i class="icon-cart"></i>
            </button>
            <p class="countPrice"><span>&yen;</span>{{ countPrice }}</p>
            <div class="shoppingCartText" :class="{ cartTextActive: isCartTextActive }">
                <h3>注文カート</h3>
                <ul>
                    <li v-for="(cart, i) in carts" :key="i">
                        <p>{{ cart.name }}</p>
                        <p>
                            <i class="icon-minus-outline" @click="minus(i)"></i>
                            <span>{{ cart.number }}</span>
                            <i class="icon-add-solid" @click="plus(i)"></i>
                        </p>
                    </li>
                </ul>
                <p>
                    合計<span>{{ countNumber }}</span>点
                </p>
                <button @click="addHist()">注文する</button>
                <button @click="closeCart()">キャンセル</button>
            </div>
            <div class="ordersText">
                <button class="closeBtn" @click="closeAccounting()">
                    &times;
                </button>
                <h3>注文明細</h3>
                <ul>
                    <li v-for="(hist, i) in orderHists" :key="i">
                        <p>
                            {{ hist.name }}
                            <span>&times;</span>
                            {{ hist.number }}
                        </p>
                        <p :style="{
                            color: statusColor(hist.status),
                        }">
                            {{ statusValue(hist.status) }}
                        </p>
                    </li>
                </ul>
                <li class="totalPrice">合計金額: {{ countPrice }}</li>
                <P class="payMethod">
                    支払い方法 :
                <div class="radio">
                    <label><input type="radio" name="status" value="1" v-model="status" />現金
                    </label>
                    <label><input type="radio" name="status" value="2" v-model="status" />クレジットカード
                    </label>
                    <button class="reqAccounting" @click="accounting">
                        会計する
                    </button>
                </div>
                </P>
            </div>
        </footer>
        <thanksModal :isVisible.sync="showModal" v-if="showModal" />
        <bookedModal :isVisible.sync="showBookedModal" v-if="showBookedModal" /> -->
    </section>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { fetDishCategory } from "@/api/orderApi";
import { useRouter, useRoute } from "vue-router";

const res = ref([]);
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


</script>

<style lang="less" scoped>
@import '@/assets/css/phone_index.less';
</style>
