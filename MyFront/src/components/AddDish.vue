<template>
    <section id='add_dish' v-if="isVisible">
        <div class="dialog">
            <!-- <h3>メニューの種類を追加</h3>
            <div><span>種類名 :</span><input type="text" v-model="data.name" /></div> -->
            <div>
                <button @click="agree()">確認</button>
                <button class="close-btn" @click="close()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { } from "vue"
defineProps(['isVisible'])
const emit = defineEmits(['close']);

const close = () => {
    // data.name = null
    emit('close');
}

</script>

<style lang="less" scoped>
@import '@/assets/css/add_dish.less';
</style>
