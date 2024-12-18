<template>
  <section id='customer_count'>
    <div class="title">
      <p>いらっしゃいませ</p>
      <p>ご来店の総人数を
        入力してください</p>
    </div>
    <div class="customer_count">
      <ul>
        <li v-for="(_, index) in seat_count" :key="index" :class="{ active: isActive[index] }"
          @click="toggleActive(index)">
          <p>{{ index + 1 }}</p>
          <p>人</p>
        </li>
      </ul>
    </div>
    <button :class="{ disabled: isDisabled }" @click="api_setCustomerCount">人数をを確認して、注文へ進む</button>
    <small>※後から来店される人数も含めてください。</small>
  </section>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue"
import { fetchDateByTableId, setCustomerCount } from "@/api/orderApi";
import { useRoute,useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();

const seat_count = ref(0)

const fetchTable = async () => {
  try {
    const response = await fetchDateByTableId({ id: route.params.desk_id });
    seat_count.value = response.data.data.seatCount; // APIからデータを取得
    setIsActive()
  } catch (err) {
    console.error("リクエストエラー:", err);
    alert("テーブルのフェッチを失敗しました。もう一度お試しください。");
  }
};

const req = reactive({
  id: route.params.desk_id,
  guestCount: 0
})
const isDisabled = ref(true)
const isActive = ref([])
const toggleActive = (index) => {
  isActive.value = new Array(seat_count.value).fill(false);
  isActive.value[index] = true
  isDisabled.value = false
  req.guestCount = index + 1
}

const setIsActive = () => {
  for (let i = 0; i < seat_count.value; i++) {
    isActive.value = new Array(seat_count.value).fill(false);
  }
}

const api_setCustomerCount = async () => {
  try {
    const res = await setCustomerCount(req);
    // レスポンスデータを取得
    const code = res.data.code; // ステータスコードを取得
    // 成功した場合
    if (code == 1) {
      alert("人数の保存を成功しました。");
      router.push(`/order/${route.params.desk_id}/1`);
    } else {
      // エラーメッセージを表示
      alert("人数の保存を失敗しました,", res.data.msg);
    }
  } catch (error) {
    // リクエスト中にエラーが発生した場合の処理
    alert("人数の保存を失敗しました,", error);
  }
}

// コンポーネントがマウントされたときにカテゴリデータを取得
onMounted(() => {
  fetchTable();
});
</script>

<style lang="less" scoped>
@import '@/assets/css/customer_count.less';
</style>
