<template>
    <section id="dishes">
        <ul>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>
        <!-- <p>Dish ID: {{ $route.params.id }}</p>
        <ul>
            <li
                v-for="(dish, i) in dishes"
                :key="i"
                :class="statusClass2(dish.status)"
            >
                <div :class="statusTextClass(dish.status)">売り切れ</div>
                <div class="dishCard" :class="statusClass1(dish.status)">
                    <div
                        class="dishImg"
                        :style="`background-image: url(${dish.image})`"
                    ></div>
                    <div class="dishInfo">
                        <p>{{ dish.name }}</p>
                        <P>{{ dish.description }}</P>
                        <P><span>&yen;</span>{{ dish.price }}</P>
                        <p>
                            <i class="icon-minus-outline" @click="minus(i)"></i>
                            <span>{{ dishCount[i] }}</span>
                            <i class="icon-add-solid" @click="plus(i)"></i>
                        </p>
                    </div>
                </div>
            </li>
            <li></li>
        </ul> -->
    </section>
</template>

<script setup>
import { } from "vue"
</script>

<style lang="less" scoped>
@import '@/assets/css/dishes.less';
</style>
