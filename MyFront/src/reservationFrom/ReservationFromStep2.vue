<template>
  <section id='step2' v-if="isVisible">
    <ReservationHearder :step="'step2'" />
    <main>
      <ul>
        <li>
          <p>担当者名前(漢字)<small>(必要)</small>: </p>
          <input type="text" v-model="req.name" @input="validateField('name')" placeholder="例: 山田太郎">
          <small v-if="errors.name" style="color: red;">{{ errors.name }}</small>
        </li>
        <li>
          <p>担当者名前(カタカナ)<small>(必要)</small>: </p>
          <input type="text" v-model="req.katakana" @input="validateField('katakana')" placeholder="例: ヤマダタロウ">
          <small v-if="errors.katakana" style="color: red;">{{ errors.katakana }}</small>
        </li>
        <li>
          <p>予約内容<small>(必要)</small>: </p>
          <textarea v-model="req.content" @input="validateField('content')" placeholder="予約内容を入力してください"></textarea>
          <small v-if="errors.content" style="color: red;">{{ errors.content }}</small>
        </li>
        <li>
          <p>人数<small>(必要)</small>: </p>
          <div>
            <input type="text" v-model="req.people" @input="validateField('people')" placeholder="例: 2">
            <p>人</p>
          </div>
          <small v-if="errors.people" style="color: red;">{{ errors.people }}</small>
        </li>
        <li>
          <p>連結先<small>(必要)</small>: </p>
          <input type="text" v-model="req.tel" @input="validateField('tel')" placeholder="例: 09012345678">
          <small v-if="errors.tel" style="color: red;">{{ errors.tel }}</small>
        </li>
        <li>
          <p>メールアドレス<small>(必要)</small>: </p>
          <input type="text" v-model="req.mail" @input="validateField('mail')" placeholder="例: example@example.com">
          <small v-if="errors.mail" style="color: red;">{{ errors.mail }}</small>
        </li>
        <li>
          <p>特記事項: </p>
          <textarea v-model="req.specialNote"></textarea>
        </li>
        <li>
          <div class="btn">
            <button @click="prev">戻る</button>
            <button @click="next">次へ</button>
          </div>
        </li>
      </ul>
    </main>
  </section>
</template>

<script setup>
import { reactive, watch } from "vue"
import ReservationHearder from "./ReservationHearder.vue";

const props = defineProps(['isVisible', 'isCompleted'])
const emit = defineEmits(['prev', 'next']);

const req = reactive({
  name: "",
  katakana: "",
  content: "",
  people: "",
  tel: "",
  mail: "",
  specialNote: "",
});

watch(
  () => props.isCompleted, // propsのisVisibleを監視
  (newVal) => {
    if (newVal) {
      req.name = "",
        req.katakana = "",
        req.content = "",
        req.people = "",
        req.tel = "",
        req.mail = "",
        req.specialNote = ""
    }
  }, { immediate: true }
);

// 错误信息
const errors = reactive({
  name: "",
  katakana: "",
  content: "",
  people: "",
  tel: "",
  mail: "",
});

// 字段验证规则
const validationRules = {
  name: /^.{1,50}$/, // 2~10个汉字
  katakana: /^.{1,50}$/, // 2~20个片假名
  content: /^.{1,100}$/, // 5~100个字符
  people: /^[1-9][0-9]*$/, // 正整数
  tel: /^\d{10,11}$/, // 电话格式
  mail: /^[^\s@]+@[^\s@]+\.[^\s@]+$/, // 邮箱格式
};

// 验证单个字段
const validateField = (field) => {
  const regex = validationRules[field];
  if (!req[field] && field !== "SpecialNote") {
    errors[field] = "この項目は必須です"; // 必填项为空
  } else if (regex && !regex.test(req[field])) {
    errors[field] = "入力形式が正しくありません"; // 格式不正确
  } else {
    errors[field] = ""; // 清除错误
  }
};

// 验证所有字段
const validateAllFields = () => {
  for (const field in validationRules) {
    validateField(field);
  }
};

// 点击「戻る」按钮
const prev = () => {
  emit("prev");
};

// 必填项非空验证
const isReqValid = () => {
  for (const key in req) {
    if (req[key] === "" && key !== "specialNote") {
      return false;
    }
  }
  return true;
};

// 所有字段无错误验证
const isCorrectedField = () => {
  for (const key in errors) {
    if (errors[key] !== "") {
      return false;
    }
  }
  return true;
};

// 点击「次へ」按钮
const next = () => {
  // 验证所有字段
  validateAllFields();

  if (!isReqValid()) {
    alert("必要の項目を入力してください");
    return;
  }

  if (!isCorrectedField()) {
    alert("すべての項目を正しい入力してください");
    return;
  }

  // 所有验证通过
  emit("next", req);
};
</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>
