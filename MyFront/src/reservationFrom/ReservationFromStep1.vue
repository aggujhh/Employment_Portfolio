<template>
    <section id='step1' v-if="isVisible">
        <ReservationHearder :step="'step1'" />
        <main>
            <h3>予約日を選ぶ<small>(必要)</small>: </h3>
            <div class="calendar">
                <div class="header">
                    <button class="prev" @click="switchMonth('prev')">◀</button>
                    <p>{{ year }}/{{ month }}</p>
                    <button class="next" @click="switchMonth('next')">▶</button>
                </div>
                <div class="main">
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
                            <td v-for="(i, index) in item" :key="index" @click="selectDate(i)"
                                :class="{ not_this_moon: !i.this_month, today: i.is_today, bookable: i.bookable, selected: i.isSelected }">
                                <div>
                                    <p>{{ i.date }}</p>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <h3>予約時間帯を選ぶ<small>(必要)</small>: </h3>
            <div class="timeRange">
                <select v-model="req.timeRange" @focus="focusSelect" @blur="blurSelect" :class="{ focus: isFocus }">
                    <option value="">--時間を選択してください--</option>
                    <option value="11:00~12:00">11:00~12:00</option>
                    <option value="12:00~13:00">12:00~13:00</option>
                    <option value="13:00~14:00">13:00~14:00</option>
                    <option value="17:00~18:00">17:00~18:00</option>
                    <option value="18:00~19:00">18:00~19:00</option>
                    <option value="19:00~20:00">19:00~20:00</option>
                </select>
            </div>
            <div class="btn">
                <button @click="close">戻る</button>
                <button @click="next">次へ</button>
            </div>
        </main>
    </section>
</template>

<script setup>
import { ref, computed, reactive, watch } from "vue"
import ReservationHearder from "./ReservationHearder.vue";
import { useCalendarStore } from '@/stores/calendarStore';

const req = reactive({
    selectedDate: "",
    timeRange: "",
})

const calendarStore = useCalendarStore();
req.selectedDate = computed({
    get: () => calendarStore.getSelectedDate(),
    set: (value) => {
        // 根据你的逻辑，这里可以解析 value，然后调用 store 的更新方法
        // 如果 value 是空字符串，表示取消选择
        if (value === "") {
            calendarStore.setSelectedDate('', '', '');
        }
    }
});

const props = defineProps(['isVisible', 'isCompleted'])
const emit = defineEmits(['close', 'next']);

watch(
    () => props.isCompleted, // propsのisVisibleを監視
    (newVal) => {
        if (newVal) {
            deselectAll()
        }
    }, { immediate: true }
);

const close = () => {
    emit('close');
}

const next = () => {
    if (req.selectedDate !== '' && req.timeRange !== '') {
        emit('next', req);
    } else {
        alert('必要の項目を入力してください')
    }
}
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
            bookable: false
        });
    }

    // 当前月所有日期
    const currentMonthDate = new Date(year, month - 1, 1);
    const today = new Date(); // 获取当前日期

    while (currentMonthDate.getMonth() === month - 1) {
        // 提前提取当前日期信息
        const currentYear = currentMonthDate.getFullYear();
        const currentMonth = currentMonthDate.getMonth();
        const currentDate = currentMonthDate.getDate();

        // 检查是否为当前日期
        const isToday =
            currentYear === today.getFullYear() &&
            currentMonth === today.getMonth() &&
            currentDate === today.getDate();

        // 检查是否可预约
        const bookable =
            currentYear > today.getFullYear() ||
            (currentYear === today.getFullYear() &&
                (currentMonth > today.getMonth() ||
                    (currentMonth === today.getMonth() && currentDate > today.getDate())));

        const isSelected = `${currentYear}-${String(currentMonth + 1).padStart(2, '0')}-${String(currentDate).padStart(2, '0')}` === calendarStore.getSelectedDate()

        addDayToWeek({
            year: currentMonthDate.getFullYear(),
            month: currentMonthDate.getMonth() + 1,
            date: currentMonthDate.getDate(),
            weekDay: currentMonthDate.getDay(),
            this_month: true,
            is_today: isToday, // 添加标识
            bookable: bookable,
            isSelected: isSelected
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
            bookable: false
        });
        nextMonthStartDate.setDate(nextMonthStartDate.getDate() + 1);
    }

    return daysOfMonth;
};

const dayArray = ref(getDaysOfMonthWithSurroundingWeeks(year.value, month.value))

const switchMonth = (direction) => {
    if (direction === "prev") {
        month.value = month.value > 1 ? month.value - 1 : 12;
        year.value = month.value === 12 ? year.value - 1 : year.value;
    } else if (direction === "next") {
        month.value = month.value < 12 ? month.value + 1 : 1;
        year.value = month.value === 1 ? year.value + 1 : year.value;
    }
    dayArray.value = getDaysOfMonthWithSurroundingWeeks(year.value, month.value)
}


const selectDate = (date) => {
    // 清除所有选中状态
    dayArray.value.forEach(row =>
        row.forEach(item => {
            item.isSelected = false;
        })
    );

    // 更新选中状态
    let flag = date.isSelected
    flag = !flag
    date.isSelected = flag;

    if (date.bookable) {
        if (date.isSelected) {
            calendarStore.setSelectedDate(date.year, date.month, date.date);
        } else {
            calendarStore.setSelectedDate('', '', '');
        }
    }
}


const isFocus = ref(false)
const focusSelect = () => {
    isFocus.value = true
}

const blurSelect = () => {
    isFocus.value = false
}

// 清除所有选中状态
const deselectAll = () => {
    dayArray.value.forEach(row =>
        row.forEach(item => {
            item.isSelected = false;
        })
    );

    req.selectedDate = ""
    req.timeRange = ""
}

</script>

<style lang="less" scoped>
@import '@/assets/css/reservationFromHome.less';
</style>
