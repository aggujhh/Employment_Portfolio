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
        <RouterView></RouterView>
    </section>
</template>

<script setup>
import { useRoute, RouterView } from "vue-router";
const route = useRoute();
</script>

<style lang="less" scoped>
@import '@/assets/css/phone_base.less';
</style>
