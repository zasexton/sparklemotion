package baaahs.net

import baaahs.proto.Message

interface Network {
    fun link(): Link

    interface Link {
        val myAddress: Address

        fun listenUdp(port: Int, udpListener: UdpListener)
        fun sendUdp(toAddress: Address, port: Int, bytes: ByteArray)
        fun broadcastUdp(port: Int, bytes: ByteArray)

        fun sendUdp(toAddress: Address, port: Int, message: Message) {
            sendUdp(toAddress, port, message.toBytes())
        }

        fun broadcastUdp(port: Int, message: Message) {
            broadcastUdp(port, message.toBytes())
        }

        fun listenTcp(port: Int, tcpServerSocketListener: TcpServerSocketListener)
        fun connectTcp(toAddress: Address, port: Int, tcpListener: TcpListener): TcpConnection
    }

    interface Address

    interface UdpListener {
        fun receive(fromAddress: Address, bytes: ByteArray)
    }

    interface TcpConnection {
        val fromAddress: Address
        val toAddress: Address
        val port: Int

        fun send(bytes: ByteArray)

        fun send(message: Message) {
            send(message.toBytes())
        }
    }

    interface TcpListener {
        fun connected(tcpConnection: TcpConnection)
        fun receive(tcpConnection: TcpConnection, bytes: ByteArray)
        fun reset(tcpConnection: TcpConnection)
    }

    interface TcpServerSocketListener {
        fun incomingConnection(fromConnection: TcpConnection): TcpListener
    }
}