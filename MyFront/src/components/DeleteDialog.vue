<template>
    <section id='delete_dialog' v-if="isVisible">
        <div class="dialog">
            <h3>本当に「{{ name }}」削除しますか？</h3>
            <div>
                <img src="@\assets\images\icon\ExclamationMark.svg " />
                <p>この操作は元に戻せません。</p>
            </div>
            <div>
                <button @click="deleteMethod()">はい</button>
                <button class="close-btn" @click="close()">いいえ</button>
            </div>
        </div>
    </section>
</template>

<script setup>
import { } from "vue"
defineProps(['isVisible', 'name', 'deleteMethod'])
const emit = defineEmits(['close']);

const close = () => {
    emit('close');
}
</script>

<style lang="less" scoped>
@import '@/assets/css/delete_dialog.less';
</style>
