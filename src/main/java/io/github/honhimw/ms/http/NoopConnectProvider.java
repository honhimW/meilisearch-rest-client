/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.honhimw.ms.http;

import io.netty.resolver.AddressResolverGroup;
import jakarta.annotation.Nonnull;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.ConnectionObserver;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.TransportConfig;

import java.net.SocketAddress;
import java.util.function.Supplier;

/**
 * @author hon_him
 * @since 2024-01-11
 */

class NoopConnectProvider implements ConnectionProvider {

    static final NoopConnectProvider INSTANCE = new NoopConnectProvider();

    @Nonnull
    @Override
    public Mono<? extends Connection> acquire(@Nonnull TransportConfig config, @Nonnull ConnectionObserver connectionObserver, Supplier<? extends SocketAddress> remoteAddress, AddressResolverGroup<?> resolverGroup) {
        return Mono.error(() -> new IllegalStateException("http-client already closed."));
    }

    @Override
    public boolean isDisposed() {
        return true;
    }
}
