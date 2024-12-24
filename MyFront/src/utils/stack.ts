// Stack.ts
export default class Stack<T> {
    private items: T[]; // 存储栈元素

    constructor(private capacity: number = Infinity) { // 默认无限容量
        this.items = [];
    }

    // 入栈操作
    push(item: T): void {
        if (this.items.length >= this.capacity) {
            this.items.shift(); // 如果超出容量，移除最早的元素
        }
        this.items.push(item);
    }

    // 出栈操作
    pop(): T | undefined {
        return this.items.pop();
    }

    // 查看栈顶元素
    peek(): T | undefined {
        return this.items[this.items.length - 1];
    }

    // 获取栈的长度
    size(): number {
        return this.items.length;
    }

    // 判断栈是否为空
    isEmpty(): boolean {
        return this.items.length === 0;
    }

    // 清空栈
    clear(): void {
        this.items = [];
    }
}
