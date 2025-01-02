<template>
    <section id='order_base'>
        <header>
            <div class="title">
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
            </div>
            <div class="desk_id">テーブル番号：{{ route.params.desk_id }}</div>
        </header>
        <RouterView :orderState="orderState"></RouterView>
    </section>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRouter, useRoute, RouterView } from "vue-router";
const route = useRoute();
const router = useRouter();

onMounted(() => {
    fetchTables();
});

/*************************************
* 存在確認のため、すべてのテーブルを取得
**************************************/
import { fetchAllTables } from "@/api/orderApi";
const tables = ref([]);

const fetchTables = async () => {
    try {
        const response = await fetchAllTables();
        tables.value = response.data.data; // APIからデータを取得
        setTableIdArray()
        checkHasTable()   
        setOrderState();   
    } catch (err) {
        console.error("リクエストエラー:", err);
        alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
    }
};


const tableIdArray = ref([]);
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

const orderState = ref('')
const setOrderState = () => {
    tables.value.forEach(item => {
        if (item.id === route.params.desk_id) {
            orderState.value = item.orderState
            return;
        }
    })
}

</script>

<style lang="less" scoped>
@import '@/assets/css/phone_base.less';
</style>
