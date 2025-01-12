<template>
    <section id='frontSetting' v-if="isVisible">
        <div class="dialog">
            <button class="close_btn" @click="close">×</button>
            <h3>設定</h3>
            <ul>
                <li>
                    <p>すべてのデスクを最初の状態に戻る</p>
                    <button class="reset_btn" @click="reset">reset</button>
                </li>
                <li>
                    <p>デスクの使用を禁止する</p>
                    <select v-model="deskId">
                        <option value="">--テーブル番号--</option>
                        <option v-for="(item, index) in availableTableArray" :key="index" :value="item">{{ item }}
                        </option>
                    </select>
                    <button @click="seed_disableDeskById">禁止</button>
                </li>
                <li>
                    <p>禁止されたテーブル: </p>
                    <div v-for="(item, index) in disableTablesArray" :key="index">
                        <p>{{ item }}</p>
                        <button @click="seed_restoreDeskById(item)">×</button>
                    </div>
                </li>
            </ul>
        </div>
    </section>
</template>

<script setup>
import { ref, watch } from "vue"

/*************************************
*モーダルを閉じる
**************************************/
const props = defineProps(['isVisible', 'tables'])
const emit = defineEmits(['close', 'refresh']);
const close = () => {
    emit('close');
}
const availableTableArray = ref([])
const disableTablesArray = ref([])
watch(
    () => props.tables,
    (newValue) => {
        availableTableArray.value = []
        disableTablesArray.value = []
        newValue.forEach(item => {
            if (item.deskState === '3') {
                disableTablesArray.value.push(item.id)
            } else {
                availableTableArray.value.push(item.id)
            }
        });
    },
);


const reset = () => {
    const proceed = (confirm("本当にすべてのデスクをリセットしてもよろしいですか？"))
    if (proceed) {
        seed_resetAllTables()
    }
}


import { resetAllTables } from "@/api/deskApi";
const seed_resetAllTables = async () => {
    try {
        const res = await resetAllTables();
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            emit('refresh');
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

/*************************************
*テーブルを一時でき停止
**************************************/
import { disableDeskById } from "@/api/deskApi";
const deskId = ref('');

const seed_disableDeskById = async () => {
    try {
        const res = await disableDeskById({ id: deskId.value });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            deskId.value = ''
            emit('refresh');
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}

/*************************************
*テーブルを再利用に設定
**************************************/
import { restoreDeskById } from "@/api/deskApi";

const seed_restoreDeskById = async (id) => {
    try {
        const res = await restoreDeskById({ id });
        const code = res.data.code; // ステータスコードを取得
        if (code === 1) {
            deskId.value = ''
            emit('refresh');
        } else {
            console.error(res.data.msg);
        }
    } catch (error) {
        // エラー処理
        console.error("リクエストエラー:", error);
    }
}



</script>

<style lang="less" scoped>
@import '@/assets/css/frontSettingDialog.less';
</style>
