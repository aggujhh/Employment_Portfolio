<template>
    <HeaderView></HeaderView>
    <NavView></NavView>
    <section id="reservation">
        <div class="calendar">
            <input type="date" v-model="calendarDate" @change="changeCalendar">
            <div class="header">
                <button class="prev" @click="switchMonth('prev')">◀</button>
                <p>{{ year }}年{{ month }}月</p>
                <button class="next" @click="switchMonth('next')">▶</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>日</th>
                        <th>月</th>
                        <th>火</th>
                        <th>水</th>
                        <th>木</th>
                        <th>金</th>
                        <th>土</th>
                    </tr>
                </thead>
                <tr v-for="(item, index) in dayArray" :key="index">
                    <td v-for="(i, index) in item" :key="index"
                        :class="{ not_this_moon: !i.this_month, today: i.is_today }">
                        <p>{{ i.date }}</p>
                    </td>
                </tr>
            </table>
        </div>
        <div class="info">
            <div class="header">
                <p>予約詳細</p>
            </div>
        </div>
        <div class="qrCode" @click="expandQrCode" :class="{ expand: isExpand }">
            <div></div>
        </div>
    </section>
</template>

<script setup>
import { ref } from "vue"

// 現在の年と月を使用
const today = new Date();
const this_year = today.getFullYear();
const this_month = today.getMonth();

const year = ref(this_year)
const month = ref(this_month + 1)

// 指定された年、月のカレンダーに前後の週を含めた日付リストを生成する関数
const getDaysOfMonthWithSurroundingWeeks = (year, month) => {
    const daysOfMonth = [];
    let daysOfWeek = [];

    // 辅助函数：将日期加入到当前周或完成一周时推入结果数组
    const addDayToWeek = (dayInfo) => {
        daysOfWeek.push(dayInfo);
        if (daysOfWeek.length === 7) {
            daysOfMonth.push([...daysOfWeek]);
            daysOfWeek = [];
        }
    };

    // 处理月份溢出
    const adjustDate = (year, month) => {
        if (month < 1) {
            return { year: year - 1, month: 12 };
        } else if (month > 12) {
            return { year: year + 1, month: 1 };
        }
        return { year, month };
    };

    // 前一个月的最后一周
    const { year: prevYear, month: prevMonth } = adjustDate(year, month - 1);
    const previousMonthLastDate = new Date(prevYear, prevMonth, 0);
    const lastDayOfPrevMonth = previousMonthLastDate.getDate();
    const lastWeekStart = lastDayOfPrevMonth - previousMonthLastDate.getDay();
    for (let day = lastWeekStart; day <= lastDayOfPrevMonth; day++) {
        const dayDate = new Date(prevYear, prevMonth - 1, day);
        addDayToWeek({
            year: dayDate.getFullYear(),
            month: dayDate.getMonth() + 1,
            date: dayDate.getDate(),
            weekDay: dayDate.getDay(),
            this_month: false,
            isToday: false,
        });
    }

    // 当前月所有日期
    const currentMonthDate = new Date(year, month - 1, 1);
    const today = new Date(); // 获取当前日期

    while (currentMonthDate.getMonth() === month - 1) {
        // 检查是否为当前日期
        const isToday =
            currentMonthDate.getFullYear() === today.getFullYear() &&
            currentMonthDate.getMonth() === today.getMonth() &&
            currentMonthDate.getDate() === today.getDate();

        addDayToWeek({
            year: currentMonthDate.getFullYear(),
            month: currentMonthDate.getMonth() + 1,
            date: currentMonthDate.getDate(),
            weekDay: currentMonthDate.getDay(),
            this_month: true,
            is_today: isToday, // 添加标识
        });

        currentMonthDate.setDate(currentMonthDate.getDate() + 1);
    }

    // 下一个月的第一周
    const { year: nextYear, month: nextMonth } = adjustDate(year, month + 1);
    const nextMonthStartDate = new Date(nextYear, nextMonth - 1, 1);
    while (daysOfWeek.length > 0 || nextMonthStartDate.getDay() !== 0) {
        addDayToWeek({
            year: nextMonthStartDate.getFullYear(),
            month: nextMonthStartDate.getMonth() + 1,
            date: nextMonthStartDate.getDate(),
            weekDay: nextMonthStartDate.getDay(),
            this_month: false,
            isToday: false,
        });
        nextMonthStartDate.setDate(nextMonthStartDate.getDate() + 1);
    }

    return daysOfMonth;
};

const dayArray = ref(getDaysOfMonthWithSurroundingWeeks(year.value, month.value))
console.log(dayArray.value);

const switchMonth = (direction) => {
    if (direction === "prev") {
        month.value = month.value > 1 ? month.value - 1 : 12;
        year.value = month.value === 12 ? year.value - 1 : year.value;
    } else if (direction === "next") {
        month.value = month.value < 12 ? month.value + 1 : 1;
        year.value = month.value === 1 ? year.value + 1 : year.value;
    }
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value)
    calendarDate.value =`${year.value}-${String(month.value).padStart(2, '0')}-01`
}


const calendarDate = ref(today.toISOString().split("T")[0])

const changeCalendar = () => {
    console.log(calendarDate.value);
    const assignDate = new Date(calendarDate.value);
    const assignMonth = assignDate.getMonth() + 1;
    const assignYear = assignDate.getFullYear();
    console.log(month, year);
    month.value = assignMonth
    year.value = assignYear
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value)
}

const isExpand = ref(false)

const expandQrCode = () => {
    isExpand.value = !isExpand.value
}
</script>

<style lang="less" scoped>
@import '@/assets/css/reservation.less';
</style>