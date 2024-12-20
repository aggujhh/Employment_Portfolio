<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="kitchen_screen">
        <div class="header">
            <div>
                <img src="@/assets/images/icon/Renew.svg" />
                <p>更新</p>
            </div>
            <div>
                <img src="@/assets/images/icon/Return.svg" />
                <p>戻る</p>
            </div>
            <div>
                <img src="@/assets/images/icon/History.svg" />
                <p>履歴</p>
            </div>
        </div>
        <div class="main">
            <ul>
                <li v-for="(order, index) in orders" :key="index" class="order"
                    :style="{ top: `${order.top}px`, left: `${order.left}px`, width: `${orderWidth}px` }">
                    <div class="order_head">
                        <h3>テーブル<span>{{ order.table }}</span></h3>
                        <p>{{ order.people }}名</p>
                    </div>
                    <div v-for="(dish, dishIndex) in order.dishes" :key="dishIndex" class="dish">
                        <div></div>
                        <p>{{ dish.name }}</p>
                        <div>
                            <p>{{ dish.quantity }}</p>
                        </div>
                    </div>
                    <div class="order-fooder">
                        <p>{{ order.elapsedTime }}</p>
                        <button>すべて調理済</button>
                    </div>
                </li>
            </ul>
        </div>
    </section>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from "vue";

// 示例数据
const orders = reactive([
    {
        table: "A1",
        people: 3,
        elapsedTime: "00:10経過",
        dishes: [
            { name: "料理1", quantity: 3 },
            { name: "料理2", quantity: 3 },
            { name: "料理3", quantity: 3 },
        ],
        height: 0,
        top: 0,
        left: 0,
    },
    {
        table: "A2",
        people: 2,
        elapsedTime: "00:15経過",
        dishes: [{ name: "料理1", quantity: 1 }],
        height: 0,
        top: 0,
        left: 0,
    },
]);

const columnCount = 5; 
const columnHeights = ref(new Array(columnCount).fill(40)); 
const orderWidth = ref(0);
// 瀑布流布局计算
const setOrderHeight = () => {
    const containerWidth = document.querySelector(".main>ul").scrollWidth; // 获取容器宽度 
    orderWidth.value = ((containerWidth - 160) / columnCount); // 每列宽度
    columnHeights.value = new Array(columnCount).fill(40); // 重置每列高度

    // 遍历每个订单，设置位置
    orders.forEach((order, index) => {
        const orderEl = document.querySelectorAll(".order")[index];
        if (orderEl) {
            const count = orderEl.querySelectorAll(".dish").length
            order.height = count * 60 + 157
        }

        // 找到最小列
        const minHeight = Math.min(...columnHeights.value);
        const columnIndex = columnHeights.value.indexOf(minHeight);

        // 设置订单位置
        order.top = minHeight;
        order.left = columnIndex * (orderWidth.value + 20) + 40;

        // 更新列高度
        columnHeights.value[columnIndex] += order.height + 20; // 加上间距
    });
};

// 页面初始化时触发
onMounted(() => {
    nextTick(() => {
        setTimeout(() => { setOrderHeight(); }, 100)

        // 初始布局计算
    });
})

</script>


<style lang="less" scoped>
@import '@/assets/css/kitchen_screen.less';
</style>
