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
        <div class="callBell" title="店員を呼び出し" @click="openModal" :class="{ calling: isCalling }">
            <img src="@/assets/images/order_img/CallBell.svg">
            <p>call</p>
        </div>
        <RouterView :orderState="orderState" @fetchTables="fetchTables"></RouterView>
        <CallCheckDialog :isVisible="isVisible.callCheck" :flag="callingFlag" @close="closeModal" @calling="calling" />
    </section>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue"
import { useRouter, useRoute, RouterView } from "vue-router";
const route = useRoute();
const router = useRouter();

/*************************************
* モデルを導入する
**************************************/
import CallCheckDialog from "@/phoneViews/CallCheckDialog.vue";
const isVisible = reactive({
    callCheck: false,
})

const callingFlag = ref("")

// モーダルを閉じる関数
const closeModal = () => {
    isVisible.callCheck = false;
};

const openModal = () => {
    if (orderState.value === '3') {
        callingFlag.value = '1'
    } else {
        callingFlag.value = '0'
    }
    isVisible.callCheck = true;
}

const isCalling = ref(false)
const calling = () => {
    fetchTables();
}


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
            router.push(`/order/${route.params.desk_id}/${route.params.nanoId}/0`);
        }
    }
};

const orderState = ref('')


const setOrderState = () => { 
    tables.value.forEach(item => {
        console.log(item.id,route.params.desk_id.toUpperCase());    
        if (item.id === route.params.desk_id.toUpperCase()) {
            orderState.value=item.orderState
            if (orderState.value === '3') {
                isCalling.value = true
            } else {
                isCalling.value = false
            }
            return;
        }
    })
}
</script>

<style lang="less" scoped>
@import '@/assets/css/phone_base.less';
</style>
