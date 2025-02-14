// stores/calendarStore.js
import { defineStore } from 'pinia';

export const useCalendarStore = defineStore('calendar', {
    state: () => ({
        selectedDate: '', // 初始化为一个空字符串
    }),
    actions: {
        /**
         * 设置选中的日期
         * @param year 年份
         * @param month 月份
         * @param date 日期
         */
        setSelectedDate(year: number, month: number, date: number): void {
            // 格式化日期为 YYYY-MM-DD
            this.selectedDate = `${year}-${String(month).padStart(2, '0')}-${String(date).padStart(2, '0')}`;
        },

        /**
         * 获取选中的日期
         * @returns 当前选中的日期字符串
         */
        getSelectedDate(): string {
            return this.selectedDate;
        },
    },
});
