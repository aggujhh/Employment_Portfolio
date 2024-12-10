<template>
    <section id="add_dish_category" v-if="isVisible">
        <div class="dialog">
            <h3>メニューの種類を追加する?</h3>
            <div><span>種類名 :</span><input type="text"/></div>
            <div>
                <button @click="agree">確認</button>
                <button class="close-btn" @click="closeModal()">戻る</button>
            </div>
        </div>
    </section>
</template>

<script setup>
defineProps(['isVisible', 'closeModal'])
</script>

<style lang="less" scoped>
@import '@/assets/css/add_dish_category.less';
</style>
